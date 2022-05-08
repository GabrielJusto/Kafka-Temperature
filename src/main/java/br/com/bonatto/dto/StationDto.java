package br.com.bonatto.dto;


import br.com.bonatto.modelo.Point;
import br.com.bonatto.modelo.Station;

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

    public StationDto(Station station)
    {
        this.local = station.getLocal();
        this.connector = station.getConnector();
        this.fastCharge = station.isFastCharge();
        this.brand = station.getBrand();
    }

}
