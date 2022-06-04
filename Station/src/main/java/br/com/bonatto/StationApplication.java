package br.com.bonatto;



public class StationApplication
{


    public static void main(String[] args) {
        //lat -29.97148 - -30.24828
        //lon -51.24001 - -51.11504

            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    long uniqueID = System.currentTimeMillis();
                    StationService service = new StationService(uniqueID, 50, 30, -30.001, -51.15, false, "B1", "CON1");
                    service.run();
                }
            });
            t.run();

            Thread t2 = new Thread(new Runnable() {
                @Override
                public void run() {
                    long uniqueID = System.currentTimeMillis();
                    StationService service = new StationService(uniqueID, 50, 30, -30.001, -51.15, false, "B1", "C1");
                    service.run();
                }
            });
            t2.run();


    }

}
