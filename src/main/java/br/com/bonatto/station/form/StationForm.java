package br.com.bonatto.station.form;

import br.com.bonatto.broker.model.Point;
import br.com.bonatto.station.model.Station;

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
