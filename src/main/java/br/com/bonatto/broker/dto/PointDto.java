package br.com.bonatto.broker.dto;

import br.com.bonatto.broker.model.Point;

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
