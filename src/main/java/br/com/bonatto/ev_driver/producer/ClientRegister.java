package br.com.bonatto.ev_driver.producer;

import br.com.bonatto.ev_driver.form.ClientForm;
import br.com.bonatto.kafka.KafkaDispatcher;
import br.com.bonatto.ev_driver.model.Client;

import java.util.concurrent.ExecutionException;

public class ClientRegister
{

    private ClientForm client;

    public ClientRegister(ClientForm client) {
        this.client = client;
    }

    public void sendRegister()
    {

        try(var clientDispatcher = new KafkaDispatcher<ClientForm>()) {


            clientDispatcher.send("CLIENT-REGISTER", client.toString(), client);



        } catch (ExecutionException | InterruptedException e) {
            System.err.println("Error to send register client message: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

}
