package br.com.bonatto.consumer;

import br.com.bonatto.ev_driver.model.Client;
import br.com.bonatto.kafka.KafkaService;
import com.influxdb.client.InfluxDBClient;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.Map;
import java.util.regex.Pattern;

public class KafkaConsumer
{

    public static void main(String[] args) {


       KafkaConsumer consumer = new KafkaConsumer();
        KafkaService<Client> service = new KafkaService(KafkaConsumer.class.getSimpleName(),
                Pattern.compile(".*"),
                consumer::parse,
                Client.class,
                Map.of("", ""));
        service.run();

        service.close();
    }


    private void parse(ConsumerRecord<String, Client> record, InfluxDBClient client, String bucket, String org) {


        System.out.println("Consume Message");
        Client c = record.value();
//        clientRepository.save(clientRegister);

    }
}
