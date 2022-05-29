package br.com.bonatto.consumer;

import br.com.bonatto.kafka.KafkaService;
import br.com.bonatto.model.Client;
import br.com.bonatto.model.Station;
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
import java.util.Map;
import java.util.regex.Pattern;

public class KafkaConsumer
{



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
                case "CLIENT-REGISTER":

                    className = Client.class.getName();
                    Client c = gson.fromJson(record.value(), (Class<Client>) Class.forName(className));
                    clientRepository.insert(c);
                    con.commit();
                    break;

                case "CLIENT-INFO" :
                    className = Client.class.getName();
                    Client clientInfo = gson.fromJson(record.value(), (Class<Client>) Class.forName(className));
                    clientRepository.update(clientInfo);
                    con.commit();
                    break;
                case "STATION-REGISTER":
                    className = Station.class.getName();
                    Station station = gson.fromJson(record.value(), (Class<Station>) Class.forName(className));
                    stationRepository.insert(station);
                    con.commit();
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
}
