package br.com.bonatto.consumer;

import br.com.bonatto.form.ClientForm;
import br.com.bonatto.form.StationForm;
import br.com.bonatto.kafka.KafkaService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.influxdb.client.InfluxDBClient;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Map;
import java.util.regex.Pattern;

public class KafkaConsumer
{

    public static void main(String[] args) {


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

            switch (record.topic()) {
                case "CLIENT-REGISTER":
                    className = ClientForm.class.getName();
                    ClientForm clientForm = gson.fromJson(record.value(), (Class<ClientForm>) Class.forName(className));
                    System.out.println(clientForm.getConnector());
                    break;
                case "STATION-REGISTER":
                    className = StationForm.class.getName();
                    StationForm stationForm = gson.fromJson(record.value(), (Class<StationForm>) Class.forName(className));
                    System.out.println(stationForm.getConnector());
                    break;
            }

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
