package br.com.bonatto.consumer;

import br.com.bonatto.model.Client;
import br.com.bonatto.repository.client.ClientRepository;
import br.com.bonatto.repository.factory.ConnectionFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.sql.Connection;
import java.sql.SQLException;

public class ClientRegister implements  Runnable{

    private ConsumerRecord<String, String> record;
    private Connection con;

    public ClientRegister(ConsumerRecord<String, String> record, Connection con)
    {
        this.record = record;
        this.con = con;
    }
    @Override
    public void run() {


        try{
            con.setAutoCommit(false);

            Gson gson = new GsonBuilder().create();
            ClientRepository clientRepository = new ClientRepository(con);

            String className = Client.class.getName();
            Client c = gson.fromJson(record.value(), (Class<Client>) Class.forName(className));
            clientRepository.insert(c);

            con.commit();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
