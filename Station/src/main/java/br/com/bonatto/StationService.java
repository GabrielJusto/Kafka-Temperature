package br.com.bonatto;

import br.com.bonatto.kafka.KafkaDispatcher;
import br.com.bonatto.model.Point;
import br.com.bonatto.model.StationInfo;

import java.util.concurrent.ExecutionException;

public class StationService
{


    private StationInfo station;

    public StationService(long stationId, double price, double temperature, double lat, double lon, boolean fastCharge, String brand, String connector)
    {
        station = new StationInfo(
                stationId,
                false,
                price,
                0,
                0,
                temperature,
                new Point(lat, lon),
                fastCharge,
                brand,
                connector);
    }


    public void run()
    {

        try(KafkaDispatcher<StationInfo> stationDispatcher = new KafkaDispatcher<>())
        {
            stationDispatcher.send("STATION-REGISTER", StationInfo.class.getSimpleName(), station);
        }
        catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
