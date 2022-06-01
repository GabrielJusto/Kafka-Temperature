package br.com.bonatto;

import br.com.bonatto.kafka.KafkaDispatcher;
import br.com.bonatto.kafka.KafkaService;
import br.com.bonatto.model.Point;
import br.com.bonatto.model.StationInfo;
import com.influxdb.client.InfluxDBClient;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

public class StationApplication
{

    private StationInfo info;

    public static void main(String[] args) {

        sendRegisterRequest();

        StationApplication app = new StationApplication();
        KafkaService registerService = new KafkaService(StationApplication.class.getSimpleName(),
                Pattern.compile("R-STATION-.*"),
                app::parseRegister,
                String.class,
                Map.of(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName()));


        registerService.run();





    }

    private void parseRegister(ConsumerRecord<String, String> record, InfluxDBClient client, String bucket, String org)
    {


            switch (record.topic()) {
                case "R-STATION-REGISTER-RESPONSE":
                    Long stationId = Long.valueOf(record.value());
                    info = new StationInfo(
                            stationId,
                            false,
                            50,
                            0,
                            0,
                            30,
                            new Point(20, 10),
                            true,
                            "B2",
                            "CON2");
                    StationInfo info = new StationInfo(stationId, false, 100, 0, 100000, 20);
                    sendStation();
                    break;
            }


    }



    private void sendStation()
    {
        try(KafkaDispatcher<StationInfo> stationDispatcher = new KafkaDispatcher<>())
        {
            stationDispatcher.send("STATION-REGISTER", StationInfo.class.getSimpleName(), info);
        }
        catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static void sendRegisterRequest() {
        try(KafkaDispatcher<String> stattionRequestDispatcher = new KafkaDispatcher<>())
        {
            stattionRequestDispatcher.send("STATION-REGISTER-REQUEST", Integer.class.getSimpleName(), "");
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    private void parse(ConsumerRecord<String, String> record, InfluxDBClient client, String bucket, String org) {


        try(KafkaDispatcher<StationInfo> infoDispatcher = new KafkaDispatcher<>()) {
            infoDispatcher.send("STATION-INFO", StationInfo.class.getSimpleName(), info);
        }
        catch (ExecutionException | InterruptedException e) {
            System.err.println("Error to send info station message: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

}
