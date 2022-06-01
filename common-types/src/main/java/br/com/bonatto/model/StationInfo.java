package br.com.bonatto.model;

public class StationInfo
{

    private long id;
    private boolean busy;
    private double price;
    private long busyTime;
    private long timeToCharge;
    private double temperature;

    private Point local;
    private boolean fastCharge;
    private String brand;
    private String connector;


    public StationInfo(long id, boolean busy, double price, long busyTime, long timeToCharge, double temperature) {
        this.id = id;
        this.busy = busy;
        this.price = price;
        this.busyTime = busyTime;
        this.timeToCharge = timeToCharge;
        this.temperature = temperature;
    }

    public StationInfo(long id, boolean busy, double price, long busyTime, long timeToCharge, double temperature, Point local, boolean fastCharge, String brand, String connector) {
        this.id = id;
        this.busy = busy;
        this.price = price;
        this.busyTime = busyTime;
        this.timeToCharge = timeToCharge;
        this.temperature = temperature;
        this.local = local;
        this.fastCharge = fastCharge;
        this.brand = brand;
        this.connector = connector;
    }

    public boolean isFastCharge() {
        return fastCharge;
    }

    public String getBrand() {
        return brand;
    }

    public String getConnector() {
        return connector;
    }

    public long getId() {
        return id;
    }

    public boolean isBusy() {
        return busy;
    }

    public double getPrice() {
        return price;
    }

    public long getBusyTime() {
        return busyTime;
    }

    public long getTimeToCharge() {
        return timeToCharge;
    }

    public double getTemperature() {
        return temperature;
    }

    public Point getLocal() {
        return local;
    }

    public void setBusy(boolean busy) {
        this.busy = busy;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setBusyTime(long busyTime) {
        this.busyTime = busyTime;
    }

    public void setTimeToCharge(long timeToCharge) {
        this.timeToCharge = timeToCharge;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }
}
