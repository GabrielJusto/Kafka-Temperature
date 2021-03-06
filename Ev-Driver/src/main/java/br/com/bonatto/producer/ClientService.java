package br.com.bonatto.producer;

import br.com.bonatto.ClientApplication;
import br.com.bonatto.kafka.KafkaDispatcher;
import br.com.bonatto.kafka.KafkaService;
import br.com.bonatto.model.Client;
import br.com.bonatto.model.Point;
import com.influxdb.client.InfluxDBClient;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class ClientService
{

    private Client client;


    public ClientService(long id, double lon, double lat, String connector, double maxPrice, String walletKey, long timeToCharge)
    {
        client = new Client(id, new Point(lon,lat) , connector, maxPrice, walletKey, timeToCharge, false);

    }

    public void run()
    {
        try(KafkaDispatcher<Client> clientDispatcher = new KafkaDispatcher<>())
        {



            createTopic("R-CLIENT-CHARGING-" + client.getId(), 1, 1, Map.of());

            clientDispatcher.send("CLIENT-REGISTER", Client.class.getSimpleName(), client);




            KafkaService chargingService = new KafkaService(ClientApplication.class.getSimpleName(),
                    "R-CLIENT-CHARGING-" + client.getId(),
                    this::parse,
                    Boolean.class,
                    Map.of());


            Thread.sleep(5000);
            clientDispatcher.send("CLIENT-INFO", Client.class.getSimpleName(), client);



            chargingService.run();
            chargingService.close();

        }catch (ExecutionException | InterruptedException e) {
            System.err.println("Error to send register client message: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private void parse(ConsumerRecord<String, Boolean> record, InfluxDBClient client, String bucket, String org) {
        boolean scheduled = record.value();



        if(scheduled)
        {
            try {
                System.out.println("carregado");
                Thread.sleep(this.client.getTimeToCharge());
                System.out.println("carregou");
                try(KafkaDispatcher<Long> clientDispatcher = new KafkaDispatcher<>()) {

                    clientDispatcher.send("CLIENT-CHARGED", Integer.class.getSimpleName(), this.client.getId());

                } catch (ExecutionException | InterruptedException  e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
                Thread.currentThread().interrupt();

            } catch (InterruptedException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }else
        {
            try(KafkaDispatcher<Client> clientDispatcher = new KafkaDispatcher<>()) {

                Thread.sleep(5000);
                clientDispatcher.send("CLIENT-INFO", Client.class.getSimpleName(), this.client);

            } catch (ExecutionException | InterruptedException  e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }



    private static void createTopic(final String topic,
                                    final int partitions,
                                    final int replication,
                                    final Map<String, String> topicConfig) {

        final Map<String, Object> props = Map.of(
                AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092",
                AdminClientConfig.RETRIES_CONFIG, 5);
        try (AdminClient adminClient = AdminClient.create(props)) {
            final NewTopic newTopic = new NewTopic(topic, partitions, (short) replication);
            newTopic.configs(topicConfig);
            try {
                final CreateTopicsResult result = adminClient.createTopics(List.of(newTopic));
                result.all().get();
            } catch (final Exception e) {
                throw new RuntimeException("Failed to create topic:" + topic, e);
            }
        }
    }


}
