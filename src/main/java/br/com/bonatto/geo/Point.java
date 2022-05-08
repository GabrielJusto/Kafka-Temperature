package br.com.bonatto.geo;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Point
{

    @Id
    private long id;
    private double lon;
    private double lat;

    public Point(double lon, double lat)
    {
        this.lon = lon;
        this.lat = lat;
    }
}
