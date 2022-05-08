package br.com.bonatto.consumer;

import br.com.bonatto.kafka.KafkaService;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.WriteApi;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import org.apache.kafka.clients.consumer.ConsumerRecord;


import java.time.Instant;
import java.util.Map;

public class TemperatureService {


    public static void main(String[] args) {

        // You can generate an API token from the "API Tokens Tab" in the UI
        String token = "vZX0TP_Th17nFbJJdcieHF8uC1mjA3vyQwSIMqYPHIJ647VNu37ZL3OXUgC-iZvoPcKCTGN0cfKeteUlRWk_BA==";
        String bucket = "Bonatto";
        String org = "Bonatto";

        InfluxDBClient client = InfluxDBClientFactory.create("http://localhost:8086", token.toCharArray());

        var emailService = new TemperatureService();
        try (var service = new KafkaService(TemperatureService.class.getSimpleName(),
                "TEMPERATURE",
                emailService::parse,
                Double.class,
                Map.of(), client, bucket, org)) {
            service.run();
        }
    }

    private void parse(ConsumerRecord<String, Double> record, InfluxDBClient client, String bucket, String org) {


        System.out.println("Consume Temperature");

        Point point = Point
                .measurement("temperature")
                .addTag("host", "host1")
                .addField("used_percent", record.value())
                .time(Instant.now(), WritePrecision.NS);

        try (WriteApi writeApi = client.getWriteApi()) {
            writeApi.writePoint(bucket, org, point);
        }

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            // ignoring
            e.printStackTrace();
        }
    }


}
