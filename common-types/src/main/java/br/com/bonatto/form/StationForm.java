package br.com.bonatto.form;


import br.com.bonatto.model.Point;
import br.com.bonatto.model.Station;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class StationForm
{
    @NotNull @NotEmpty
    private double lat;

    @NotNull @NotEmpty
    private double lon;

    @NotNull @NotEmpty
    private String connector;
    private boolean fastCharge;
    private String brand;


    public StationForm(double lat, double lon, String connector, boolean fastCharge, String brand) {
        this.lat = lat;
        this.lon = lon;
        this.connector = connector;
        this.fastCharge = fastCharge;
        this.brand = brand;
    }

    public Station convert()
    {
        return new Station(new Point(lat, lon), connector, fastCharge, brand);
    }


    public String getConnector() {
        return connector;
    }

    public boolean isFastCharge() {
        return fastCharge;
    }

    public String getBrand() {
        return brand;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }
}
