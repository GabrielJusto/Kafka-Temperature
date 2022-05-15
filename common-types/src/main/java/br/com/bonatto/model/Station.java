package br.com.bonatto.model;

import javax.persistence.*;

@Entity
public class Station
{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne
    private Point local;
    private String connector;
    private boolean fastCharge;
    private String brand;

    public Station() {
    }

    public Station(Point local, String connector, boolean fastCharge, String brand) {
        this.local = local;
        this.connector = connector;
        this.fastCharge = fastCharge;
        this.brand = brand;
    }

    public Point getLocal() {
        return local;
    }

    public long getId() {
        return id;
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
}
