package br.com.bonatto;

import br.com.bonatto.kafka.KafkaDispatcher;
import br.com.bonatto.kafka.KafkaService;
import br.com.bonatto.model.Client;
import br.com.bonatto.model.Point;
import br.com.bonatto.model.Station;
import br.com.bonatto.model.StationInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.WriteApi;
import com.influxdb.client.domain.WritePrecision;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.Instant;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;

public class StationApplication
{

    private StationInfo info;
    private KafkaService registerService;
    private Station station;

    public static void main(String[] args) {

        sendRegisterRequest();

        StationApplication app = new StationApplication();
        app.registerService = new KafkaService(StationApplication.class.getSimpleName(),
                "STATION-REGISTER-RESPONSE",
                app::parseRegister,
                Long.class,
                Map.of());


        app.registerService.run();





    }

    private void parseRegister(ConsumerRecord<String, Long> record, InfluxDBClient client, String bucket, String org)
    {
        Long stationId = record.value();
        station = new Station(stationId, new Point(20,10), "CON2", false, "B2");
        try(KafkaDispatcher<Station> stationDispatcher = new KafkaDispatcher<>())
        {


            stationDispatcher.send("STATION-REGISTER", Station.class.getSimpleName(), station);
            registerService.close();
//            StationInfo info = new StationInfo(stationId, false, 100, 0, 100000, 20);
//
//
//
//            StationApplication stationApp = new StationApplication();
//            stationApp.info = info;
//            KafkaService service = new KafkaService(
//                    StationApplication.class.getSimpleName(),
//                    "STATION-UPDATE-REQUEST",
//                    stationApp::parse,
//                    String.class,
//                    Map.of(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName()));
//
//            service.run();
//
//            service.close();

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
