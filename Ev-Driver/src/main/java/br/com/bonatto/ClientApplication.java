package br.com.bonatto;


import br.com.bonatto.producer.ClientService;
import com.sun.xml.bind.v2.model.annotation.RuntimeAnnotationReader;

import java.util.Random;

public class ClientApplication
{

    public static void main(String[] args) throws InterruptedException {

        Random r = new Random();

        for(int i = 0; i<1; i++)
        {
            //lat -29.97148 - -30.24828
            //lon -51.24001 - -51.11504
            double rand= r.nextDouble();
            double lat = -30.24828 +(-29.97148 * rand);
            rand = r.nextDouble();
            double lon = -51.24001 +(-51.11504 * rand);
            if(i<=20)
            {
                final int id = i;
                Thread t1 = new Thread(() -> {
                    ClientService service = new ClientService(id, lon, lat, "CON1",100, "WALLET", 15000);
                    service.run();
                });

                t1.start();
            } else if (i<=40)
            {
                final int id = i;
                Thread t1 = new Thread(() -> {
                    ClientService service = new ClientService(id, lon, lat, "CON1",50, "WALLET", 10000);
                    service.run();
                });

                t1.start();
            }else if(i<=60)
            {
                final int id = i;
                Thread t1 = new Thread(() -> {
                    ClientService service = new ClientService(id, lon, lat, "CON2",200, "WALLET", 30000);
                    service.run();
                });

                t1.start();
            }else if(i<=80)
            {
                final int id = i;
                Thread t1 = new Thread(() -> {
                    ClientService service = new ClientService(id, lon, lat, "CON3",50, "WALLET", 30000);
                    service.run();
                });

                t1.start();
            }else
            {
                final int id = i;
                Thread t1 = new Thread(() -> {
                    ClientService service = new ClientService(id, lon, lat, "CON3",200, "WALLET", 10000);
                    service.run();
                });

                t1.start();
            }
        }
    }




}
