package br.com.bonatto.dto;


import br.com.bonatto.geo.Point;

public class StationDto
{
    private Point local;
    private String connector;
    private boolean fastCharge;
    private String brand;

    public StationDto(Point local, String connector, boolean fastCharge, String brand) {
        this.local = local;
        this.connector = connector;
        this.fastCharge = fastCharge;
        this.brand = brand;
    }

}
