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

    public static double distance(Point p1, Point p2)
    {
        return Math.sqrt((Math.pow(p1.lat,2) - Math.pow(p2.lat,2)) + (Math.pow(p1.lon,2) - Math.pow(p2.lon,2)));
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
