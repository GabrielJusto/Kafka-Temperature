package br.com.bonatto.model;


public class Point
{

    private long id;
    private double lon;
    private double lat;

    public Point(double lon, double lat)
    {
        this.lon = lon;
        this.lat = lat;
    }


    public long getId() {
        return id;
    }

    public double getLon() {
        return lon;
    }

    public double getLat() {
        return lat;
    }
}
