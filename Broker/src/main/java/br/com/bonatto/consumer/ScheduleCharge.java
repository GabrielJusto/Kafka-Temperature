package br.com.bonatto.consumer;

import br.com.bonatto.kafka.KafkaDispatcher;
import br.com.bonatto.model.Client;
import br.com.bonatto.model.StationInfo;
import br.com.bonatto.repository.client.ClientRepository;
import br.com.bonatto.repository.factory.ConnectionFactory;
import br.com.bonatto.repository.station.StationRepository;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class ScheduleCharge implements Runnable {

    private ConsumerRecord<String, String> record;
    private HashMap<Long, Long> chargingMap ;
    private Connection con;

    public ScheduleCharge(ConsumerRecord<String, String> record, HashMap<Long, Long> chargingMap, Connection con) {
        this.record = record;
        this.chargingMap = chargingMap;
        this.con = con;
    }

    @Override
    public void run() {

        ConnectionFactory factory = new ConnectionFactory();
        try (Connection con = factory.recuperarConexao()){
            con.setAutoCommit(false);
            Gson gson = new GsonBuilder().create();

            String className = Client.class.getName();
            Client clientInfo = gson.fromJson(record.value(), (Class<Client>) Class.forName(className));

            scheduleCharge(con, clientInfo);
            con.commit();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    private void scheduleCharge(Connection con, Client c) throws SQLException {


        boolean scheduled = false;

        //Inicializa os repositorios para alterar o status da estacao e do cliente se for encontrada uma estacao
        StationRepository stationRepository = new StationRepository(con);
        ClientRepository clientRepository = new ClientRepository(con);

        //Verivica se o cliente ja esta carregando
        Client c1 = clientRepository.getById(c.getId());
        if(c1.isCharging())
            return;

        ArrayList<StationInfo> stationsList = stationRepository.search();

        br.com.bonatto.model.Point cLocal = c.getLocal();
        StationInfo closestStation = null;

        for(int i=0; i<stationsList.size(); i++) {
            StationInfo s = stationsList.get(i);

            //Encontra a primeira estacao compativel
            if(closestStation == null) {
                if (!s.isBusy() && s.getConnector().equals(c1.getConnector()))
                    closestStation = s;
            }else {
                s = stationsList.get(i);
                if(!s.isBusy() && s.getConnector().equals(c1.getConnector())
                        && br.com.bonatto.model.Point.distance(s.getLocal(), c1.getLocal()) < br.com.bonatto.model.Point.distance(closestStation.getLocal(), c.getLocal()))
                    closestStation = s;

            }

        }

        if(closestStation != null) {
            closestStation.setBusy(true);
            stationRepository.update(closestStation);

            c1.setCharging(true);
            clientRepository.update(c1);

            scheduled = true;

            chargingMap.put(c1.getId(), closestStation.getId());
        }
        sendClientCharging(c1, scheduled);

    }


    private void sendClientCharging(Client c, boolean scheduled)
    {
        try(KafkaDispatcher<Boolean> chargingDispatcher = new KafkaDispatcher<>()) {
            chargingDispatcher.send("R-CLIENT-CHARGING-"+String.valueOf(c.getId()), Boolean.class.getSimpleName(), scheduled);

        } catch (ExecutionException | InterruptedException | RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

}
