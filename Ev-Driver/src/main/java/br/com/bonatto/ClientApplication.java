package br.com.bonatto;

import br.com.bonatto.broker.model.Point;
import br.com.bonatto.kafka.KafkaDispatcher;
import br.com.bonatto.model.Client;

import java.util.concurrent.ExecutionException;

public class ClientApplication
{
    public static void main(String[] args) {

        try(KafkaDispatcher<Client> clientDispatcher = new KafkaDispatcher<>())
        {
            clientDispatcher.send("CLIENT-REGISTER", "Client",
                    new Client(new Point(10,10), "CON", 200, "WALLET"));



        }catch (ExecutionException | InterruptedException e) {
            System.err.println("Error to send register client message: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
