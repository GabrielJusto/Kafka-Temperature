package br.com.bonatto.producer;

import br.com.bonatto.kafka.KafkaDispatcher;
import br.com.bonatto.model.Client;

import java.util.concurrent.ExecutionException;

public class ClientRegisterProducer
{

    private Client client;

    public ClientRegisterProducer(Client client) {
        this.client = client;
    }

    public void sendRegister()
    {

        try(var clientDispatcher = new KafkaDispatcher<Client>()) {


            clientDispatcher.send("CLIENT-REGISTER", client.toString(), client);



        } catch (ExecutionException | InterruptedException e) {
            System.err.println("Error to send register client message: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
