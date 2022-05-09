package br.com.bonatto.modelo;

import javax.persistence.*;

@Entity
public class Client
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @OneToOne
    private Point local;
    private String connector;
    private double maxPrice;
    private String walletKey;

    public Client(Point local, String connector, double maxPrice, String walletKey) {
        this.local = local;
        this.connector = connector;
        this.maxPrice = maxPrice;
        this.walletKey = walletKey;
    }

    public Client(){}

    public long getId() {
        return id;
    }

    public Point getLocal() {
        return local;
    }

    public String getConnector() {
        return connector;
    }

    public double getMaxPrice() {
        return maxPrice;
    }

    public String getWalletKey() {
        return walletKey;
    }
}
