package br.com.bonatto.broker.consumer;

import br.com.bonatto.broker.repository.ClientRepository;
import br.com.bonatto.ev_driver.form.ClientForm;
import br.com.bonatto.ev_driver.model.Client;
import br.com.bonatto.kafka.KafkaService;
import com.influxdb.client.InfluxDBClient;
import com.mysql.cj.xdevapi.ClientFactory;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.repository.core.support.ReactiveRepositoryFactorySupport;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import org.springframework.orm.jpa.LocalEntityManagerFactoryBean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service("clientRegister")
public class ClientRegisterConsumer
{

    @Autowired
    private ClientRepository clientRepository;

    public void consume()
    {


        ClientRegisterConsumer clientRegisterService = new ClientRegisterConsumer();

        KafkaService service = new KafkaService(ClientRegisterConsumer.class.getSimpleName(),
                "CLIENT-REGISTER",
                clientRegisterService::parse,
                ClientForm.class,
                Map.of());
        service.run();

        service.close();

    }






    private void parse(ConsumerRecord<String, ClientForm> record, InfluxDBClient client, String bucket, String org) {


        System.out.println("Consume ClientRecord");
        ClientForm c = record.value();
        Client clientRegister = c.convert();
        clientRepository.save(clientRegister);

    }

}


