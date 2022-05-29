package br.com.bonatto.model;

public class Client
{

    private int id;
    private Point local;
    private String connector;
    private double maxPrice;
    private String walletKey;

    public Client(int id, Point local, String connector, double maxPrice, String walletKey) {
        this.id = id;
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

    public void setLocal(Point local) {
        this.local = local;
    }

    public void setConnector(String connector) {
        this.connector = connector;
    }

    public void setMaxPrice(double maxPrice) {
        this.maxPrice = maxPrice;
    }

    public void setWalletKey(String walletKey) {
        this.walletKey = walletKey;
    }
}
