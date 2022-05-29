package br.com.bonatto.model;


public class Point
{

    private int id;
    private double lon;
    private double lat;

    public Point(double lon, double lat)
    {
        this.lon = lon;
        this.lat = lat;
    }

    public Point(int id, double lon, double lat) {
        this.id = id;
        this.lon = lon;
        this.lat = lat;
    }

    public int getId() {
        return id;
    }

    public double getLon() {
        return lon;
    }

    public double getLat() {
        return lat;
    }
}
