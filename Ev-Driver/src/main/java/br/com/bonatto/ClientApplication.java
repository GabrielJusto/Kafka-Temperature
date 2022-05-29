package br.com.bonatto;

import br.com.bonatto.kafka.KafkaDispatcher;
import br.com.bonatto.model.Client;
import br.com.bonatto.model.Point;

import java.util.concurrent.ExecutionException;

public class ClientApplication
{
    public static void main(String[] args) {

        try(KafkaDispatcher<Client> clientDispatcher = new KafkaDispatcher<>())
        {
            int uniqueID = (int) (System.currentTimeMillis()%999999);
            Client client = new Client(uniqueID, new Point(10,10) , "CON2", 50, "WALLET123");
            clientDispatcher.send("CLIENT-REGISTER", Client.class.getSimpleName(), client);


            while(true)
            {
                clientDispatcher.send("CLIENT-INFO", Client.class.getSimpleName(), client);
            }

        }catch (ExecutionException | InterruptedException e) {
            System.err.println("Error to send register client message: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
