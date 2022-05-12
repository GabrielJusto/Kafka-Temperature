package br.com.bonatto;

import br.com.bonatto.broker.model.Point;
import br.com.bonatto.kafka.KafkaDispatcher;
import br.com.bonatto.station.model.Station;

import java.util.concurrent.ExecutionException;

public class StationApplication
{
    public static void main(String[] args) {

        try(KafkaDispatcher<Station> clientDispatcher = new KafkaDispatcher<>())
        {
            clientDispatcher.send("STATION-REGISTER", "Client",
                    new Station(new Point(10,10), "CON", true, "B1"));



        }catch (ExecutionException | InterruptedException e) {
            System.err.println("Error to send register client message: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
