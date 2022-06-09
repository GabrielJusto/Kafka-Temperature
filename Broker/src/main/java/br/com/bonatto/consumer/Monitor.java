package br.com.bonatto.consumer;

import br.com.bonatto.model.StationInfo;
import br.com.bonatto.repository.factory.ConnectionFactory;
import br.com.bonatto.repository.station.StationRepository;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.WriteApi;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import org.apache.kafka.common.protocol.types.Field;

import java.sql.Array;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Monitor implements Runnable{


    @Override
    public void run() {


        String token = "qgRk4a-LBe6XqqkhtaMTQx_-zRQXvdlq4eWv2HPi57Z7aof0pxY7giPZVvEygaOJVWnt9yR_0eeBDTG04eKgcw==";
        String bucket = "Bonatto";
        String org = "Bonatto";


        InfluxDBClient client = InfluxDBClientFactory.create("http://localhost:8086", token.toCharArray());

        ConnectionFactory connectionFactory = new ConnectionFactory();
        try(Connection con = connectionFactory.recuperarConexao())
        {
            StationRepository stationRepository = new StationRepository(con);
            ArrayList<StationInfo> stationList = new ArrayList<>();

            while (true) {
                stationList = stationRepository.search();

                List<StationInfo> listBusy = stationList.stream().filter(StationInfo::isBusy).toList();
                long busy = listBusy.size();

                List<String> listConnectors = stationList.stream().map( StationInfo::getConnector).distinct().toList();

                double occupation = 0;
                if (stationList.size() != 0)
                    occupation = ((double)busy / stationList.size())*100;

                for(int i=0; i<listConnectors.size(); i++)
                {
                    Point p = Point
                            .measurement("Occupation_CON_"+i)
                            .addTag("conn", i+"")
                            .addField("used_percent", occupation)
                            .time(Instant.now(), WritePrecision.NS);

                    try (WriteApi writeApi = client.getWriteApi()) {
                        writeApi.writePoint(bucket, org, p);
                    }
                }

                Point point = Point
                        .measurement("Occupation")
                        .addTag("conn", "all")
                        .addField("used_percent", occupation)
                        .time(Instant.now(), WritePrecision.NS);
                try (WriteApi writeApi = client.getWriteApi()) {
                    writeApi.writePoint(bucket, org, point);
                }

                Thread.sleep(5000);
            }
        } catch (SQLException | InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }


    }
}
