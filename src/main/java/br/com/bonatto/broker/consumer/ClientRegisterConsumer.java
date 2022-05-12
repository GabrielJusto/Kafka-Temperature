package br.com.bonatto.broker.consumer;

import br.com.bonatto.broker.repository.ClientRepository;
import br.com.bonatto.ev_driver.model.Client;
import br.com.bonatto.kafka.KafkaService;
import com.influxdb.client.InfluxDBClient;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.regex.Pattern;

@Service("clientRegister")
public class ClientRegisterConsumer
{

    @Autowired
    private ClientRepository clientRepository;

    public void consume()
    {


        ClientRegisterConsumer clientRegisterService = new ClientRegisterConsumer();


        KafkaService<Client> service = new KafkaService(ClientRegisterConsumer.class.getSimpleName(),
                Pattern.compile(".*"),
                clientRegisterService::parse,
                Client.class,
                Map.of("", ""));
        service.run();

        service.close();

    }






    private void parse(ConsumerRecord<String, Client> record, InfluxDBClient client, String bucket, String org) {


        System.out.println("Consume ClientRecord");
        Client c = record.value();
//        clientRepository.save(clientRegister);

    }

}


