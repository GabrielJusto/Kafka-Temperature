package br.com.bonatto.dto;

import br.com.bonatto.modelo.Point;

public class PointDto
{

    private double lat;
    private double lon;

    public PointDto (Point p)
    {
        this.lat = p.getLat();
        this.lon = p.getLon();
    }
}
