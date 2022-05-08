package br.com.bonatto.modelo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Point
{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
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
