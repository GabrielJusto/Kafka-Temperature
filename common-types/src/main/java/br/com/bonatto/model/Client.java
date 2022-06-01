package br.com.bonatto.model;



public class Client
{

    private long id;

    private Point local;
    private String connector;
    private double maxPrice;
    private String walletKey;

    private long timeToCharge;

    private boolean charging;

    public Client(long id, Point local, String connector, double maxPrice, String walletKey, long timeToCharge, boolean charging) {
        this.id = id;
        this.local = local;
        this.connector = connector;
        this.maxPrice = maxPrice;
        this.walletKey = walletKey;
        this.timeToCharge = timeToCharge;
        this.charging = charging;
    }


    public Client(){}


    public void setLocal(Point local) {
        this.local = local;
    }

    public void setTimeToCharge(long timeToCharge) {
        this.timeToCharge = timeToCharge;
    }

    public void setCharging(boolean charging) {
        this.charging = charging;
    }

    public long getTimeToCharge() {
        return timeToCharge;
    }

    public boolean isCharging() {
        return charging;
    }


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
