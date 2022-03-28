package br.com.bonatto.consumer;

import br.com.bonatto.kafka.KafkaService;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.WriteApi;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.time.Instant;
import java.util.Map;

public class TemperatureService {

    private static Logger logger = LogManager.getLogger(TemperatureService.class);

    public static void main(String[] args) {

        // You can generate an API token from the "API Tokens Tab" in the UI
        String token = "ZeYEyj14Dx8hPFKJfmP1o8qacoxIQcr3eKyBhIG435WaBae8NI1Y4WcEwPahZyTMnY3r3O1hx4U0yw997Lpa6w==";
        String bucket = "bonatto bucket";
        String org = "bonatto";

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

//        logger.debug("Temperature: "+record.value());
//        System.out.println("------------------------------------------");
        System.out.println("Consume Temperature");
//        System.out.println(record.value());

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
