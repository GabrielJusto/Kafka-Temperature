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
    private HashMap<Long, Long> chargingMap = new HashMap<>();
    private Connection con;

    public void consume() throws SQLException {

        String token = "vZX0TP_Th17nFbJJdcieHF8uC1mjA3vyQwSIMqYPHIJ647VNu37ZL3OXUgC-iZvoPcKCTGN0cfKeteUlRWk_BA==";
        String bucket = "Bonatto";
        String org = "Bonatto";



        InfluxDBClient client = InfluxDBClientFactory.create("http://localhost:8086", token.toCharArray());
        ConnectionFactory factory = new ConnectionFactory();


        Thread threadMonitor = new Thread(new Monitor());
        threadMonitor.start();

        KafkaConsumer consumer = new KafkaConsumer();
        consumer.con = factory.recuperarConexao();
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
        con.close();
    }


    private void parse(ConsumerRecord<String, String> record, InfluxDBClient client, String bucket, String org) {




        Gson gson = new GsonBuilder().create();




        StationRepository stationRepository = new StationRepository(con);

        switch (record.topic()) {
            case "CLIENT-CHARGED":
                freeStation(record.value(), con);
                break;
            case "CLIENT-REGISTER":
                Thread t1 = new Thread(new ClientRegister(record, con));
                t1.start();
                break;

            case "CLIENT-INFO" :
               Thread t2 = new Thread(new ScheduleCharge(record, chargingMap, con));
               t2.start();

                break;
            case "STATION-REGISTER":
                sendStationInfoRequest(gson, record, stationRepository, con);
                break;
            case "STATION-INFO":


        }

    }





    private void freeStation(String clientIdStr, Connection con)
    {
        long clientId = Long.parseLong(clientIdStr);
        long stationId = chargingMap.get(clientId);

        System.out.println("free station: " + stationId);
        StationRepository stationRepository = new StationRepository(con);
        stationRepository.free(stationId);

    }


    private void sendStationInfoRequest(Gson gson, ConsumerRecord<String, String> record, StationRepository stationRepository, Connection con)
    {
        try {

            String className = StationInfo.class.getName();
            StationInfo station = gson.fromJson(record.value(), (Class<StationInfo>) Class.forName(className));
            stationRepository.insert(station);


        } catch ( RuntimeException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
