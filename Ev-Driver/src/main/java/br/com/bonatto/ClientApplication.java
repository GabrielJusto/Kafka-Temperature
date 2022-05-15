package br.com.bonatto;

import br.com.bonatto.form.ClientForm;
import br.com.bonatto.kafka.KafkaDispatcher;
import br.com.bonatto.model.Client;
import br.com.bonatto.model.Point;

import java.util.concurrent.ExecutionException;

public class ClientApplication
{
    public static void main(String[] args) {

        try(KafkaDispatcher<ClientForm> clientDispatcher = new KafkaDispatcher<>())
        {
            clientDispatcher.send("CLIENT-REGISTER", "Client",
                    new ClientForm(10,10 , "CON", 200, "WALLET"));



        }catch (ExecutionException | InterruptedException e) {
            System.err.println("Error to send register client message: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
