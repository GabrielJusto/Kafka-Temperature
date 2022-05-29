package br.com.bonatto;

import br.com.bonatto.kafka.KafkaDispatcher;
import br.com.bonatto.model.Point;
import br.com.bonatto.model.Station;
import br.com.bonatto.model.StationInfo;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;

public class StationApplication
{
    public static void main(String[] args) {

        try(KafkaDispatcher<Station> clientDispatcher = new KafkaDispatcher<>())
        {
            int uniqueID = (int) (System.currentTimeMillis()%999999);

            clientDispatcher.send("STATION-REGISTER", Station.class.getSimpleName(),
                    new Station(uniqueID, new Point(20,10), "CON2", false, "B2"));

            StationInfo info = new StationInfo(uniqueID, false, 100, 0, 100000, 20);

            try(KafkaDispatcher<StationInfo> infoDispatcher = new KafkaDispatcher<>())
            {
                int i=1;
                while(true)
                {
                    Random r = new Random();
                    double price = r.nextDouble() * 500;
                    info.setPrice(price);
                    info.setBusy(r.nextBoolean());
                    infoDispatcher.send("STATION-INFO", StationInfo.class.getSimpleName(), info);
                    Thread.sleep(5000);

                }
            }



        }catch (ExecutionException | InterruptedException e) {
            System.err.println("Error to send register client message: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
