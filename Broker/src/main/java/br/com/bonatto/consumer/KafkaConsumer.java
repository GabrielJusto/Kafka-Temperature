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
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;


import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.regex.Pattern;

public class KafkaConsumer
{



    public void consume() {


       KafkaConsumer consumer = new KafkaConsumer();
        KafkaService service = new KafkaService(KafkaConsumer.class.getSimpleName(),
                Pattern.compile(".*"),
                consumer::parse,
                String.class,
                Map.of(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName()));
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

            switch (record.topic()) {
                case "CLIENT-REGISTER":
                    ClientRepository clientRepository = new ClientRepository(con);
                    className = Client.class.getName();
                    Client c = gson.fromJson(record.value(), (Class<Client>) Class.forName(className));
                    clientRepository.insert(c);
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

            }

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
