package br.com.bonatto.model;


public class Station
{
    private long id;

    private Point local;
    private String connector;
    private boolean fastCharge;
    private String brand;

    public Station() {
    }

    public Station(long id, Point local, String connector, boolean fastCharge, String brand) {
        this.id = id;
        this.local = local;
        this.connector = connector;
        this.fastCharge = fastCharge;
        this.brand = brand;
    }

    public Point getLocal() {
        return local;
    }



    public String getConnector() {
        return connector;
    }

    public boolean isFastCharge() {
        return fastCharge;
    }

    public String getBrand() {
        return brand;
    }

    public long getId() {
        return id;
    }
}
