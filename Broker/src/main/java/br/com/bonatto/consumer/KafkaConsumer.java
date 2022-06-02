package br.com.bonatto.consumer;

import br.com.bonatto.kafka.KafkaDispatcher;
import br.com.bonatto.kafka.KafkaService;
import br.com.bonatto.model.Client;
import br.com.bonatto.model.StationInfo;
import br.com.bonatto.repository.client.ClientRepository;
import br.com.bonatto.repository.factory.ConnectionFactory;
import br.com.bonatto.repository.station.StationRepository;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.WriteApi;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;


import java.sql.Connection;
import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

public class KafkaConsumer
{


    /*Mapeia o id do cliente para o id da estacao que ele esta carregando*/
    private HashMap<Long, Long> chargingMap = new HashMap<>();;

    public void consume() {

        String token = "vZX0TP_Th17nFbJJdcieHF8uC1mjA3vyQwSIMqYPHIJ647VNu37ZL3OXUgC-iZvoPcKCTGN0cfKeteUlRWk_BA==";
        String bucket = "Bonatto";
        String org = "Bonatto";



        InfluxDBClient client = InfluxDBClientFactory.create("http://localhost:8086", token.toCharArray());

       KafkaConsumer consumer = new KafkaConsumer();
        KafkaService service = new KafkaService(KafkaConsumer.class.getSimpleName(),
                Pattern.compile(".*"),
                consumer::parse,
                String.class,
                Map.of(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName()),
                client,
                bucket,
                org);
        service.run();

        service.close();
    }


    private void parse(ConsumerRecord<String, String> record, InfluxDBClient client, String bucket, String org) {

        try {


            Gson gson = new GsonBuilder().create();
            String className;
            ConnectionFactory factory = new ConnectionFactory();
            Connection con = factory.recuperarConexao();
            con.setAutoCommit(false);

            StationRepository stationRepository = new StationRepository(con);
            ClientRepository clientRepository = new ClientRepository(con);

            switch (record.topic()) {
                case "CLIENT-CHARGED":
                    freeStation(record.value(), con);
                    break;
                case "CLIENT-REGISTER":
                    className = Client.class.getName();
                    Client c = gson.fromJson(record.value(), (Class<Client>) Class.forName(className));
                    clientRepository.insert(c);
                    con.commit();
                    break;

                case "CLIENT-INFO" :
                    className = Client.class.getName();
                    Client clientInfo = gson.fromJson(record.value(), (Class<Client>) Class.forName(className));

                    scheduleCharge(con, clientInfo);
                    break;
                case "STATION-REGISTER":
                    sendStationInfoRequest(gson, record, stationRepository, con);
                    break;
                case "STATION-REGISTER-REQUEST":
                    senStationId();
                    break;
                case "STATION-INFO":
                    className = StationInfo.class.getName();
                    StationInfo stationInfo = gson.fromJson(record.value(), (Class<StationInfo>) Class.forName(className));
                    stationRepository.update(stationInfo);
                    con.commit();

                    Point point = Point
                            .measurement("StationPrice")
                            .addTag("host", "host1")
                            .addField("used_percent", stationInfo.getPrice())
                            .time(Instant.now(), WritePrecision.NS);
                    Point p2 = Point
                            .measurement("StationBusy")
                            .addTag("host", "host1")
                            .addField("used_percent", stationInfo.isBusy())
                            .time(Instant.now(), WritePrecision.NS);


                    try (WriteApi writeApi = client.getWriteApi()) {
                        writeApi.writePoint(bucket, org, point);
                        writeApi.writePoint(bucket, org, p2);
                    }

            }

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }




    private void scheduleCharge(Connection con, Client c) throws SQLException {


        boolean scheduled = false;

        //Inicializa os repositorios para alterar o status da estacao e do cliente se for encontrada uma estacao
        StationRepository stationRepository = new StationRepository(con);
        ClientRepository clientRepository = new ClientRepository(con);

        //Verivica se o cliente ja esta carregando
        Client c1 = clientRepository.getById(c.getId());
        if(c1.isCharging())
            return;

        ArrayList<StationInfo> stationsList = stationRepository.search();

        br.com.bonatto.model.Point cLocal = c.getLocal();
        StationInfo closestStation = null;

        for(int i=0; i<stationsList.size(); i++) {
            StationInfo s = stationsList.get(i);

            //Encontra a primeira estacao compativel
            if(closestStation == null) {
                if (!s.isBusy() && s.getConnector().equals(c1.getConnector()))
                    closestStation = s;
            }else {
                s = stationsList.get(i);
                if(!s.isBusy() && s.getConnector().equals(c1.getConnector())
                        && br.com.bonatto.model.Point.distance(s.getLocal(), c1.getLocal()) < br.com.bonatto.model.Point.distance(closestStation.getLocal(), c.getLocal()))
                    closestStation = s;

            }

        }

        if(closestStation != null) {
            closestStation.setBusy(true);
            stationRepository.update(closestStation);

            c1.setCharging(true);
            clientRepository.update(c1);

            scheduled = true;

            chargingMap.put(c1.getId(), closestStation.getId());
        }
        sendClientCharging(c1, scheduled);

        con.commit();
    }



    private void freeStation(String clientIdStr, Connection con)
    {
        try {
            long clientId = Long.parseLong(clientIdStr);
            long stationId = chargingMap.get(clientId);

            System.out.println("free station: " + stationId);
            StationRepository stationRepository = new StationRepository(con);
            stationRepository.free(stationId);


            con.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendClientCharging(Client c, boolean scheduled)
    {
        try(KafkaDispatcher<Boolean> chargingDispatcher = new KafkaDispatcher<>()) {
            chargingDispatcher.send("R-CLIENT-CHARGING-"+String.valueOf(c.getId()), Boolean.class.getSimpleName(), scheduled);

        } catch (ExecutionException | InterruptedException | RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    private void senStationId() {
        try(KafkaDispatcher<Long> stationIdDispatcher = new KafkaDispatcher<>()) {
            stationIdDispatcher.send("R-STATION-REGISTER-RESPONSE", StationInfo.class.getSimpleName(), System.currentTimeMillis());

        } catch (ExecutionException | InterruptedException | RuntimeException e) {
            throw new RuntimeException(e);
        }
    }


    private void sendStationInfoRequest(Gson gson, ConsumerRecord<String, String> record, StationRepository stationRepository, Connection con)
    {
        try {

            String className = StationInfo.class.getName();
            StationInfo station = gson.fromJson(record.value(), (Class<StationInfo>) Class.forName(className));
            stationRepository.insert(station);
            con.commit();

        } catch ( SQLException | RuntimeException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
