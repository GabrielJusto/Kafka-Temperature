package br.com.bonatto.form;

import br.com.bonatto.geo.Point;
import br.com.bonatto.modelo.Station;

import javax.persistence.OneToOne;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class StationForm
{
    @NotNull @NotEmpty
    private Point local;
    @NotNull @NotEmpty
    private String connector;
    private boolean fastCharge;
    private String brand;

    public Station convert()
    {
        return new Station(local, connector, fastCharge, brand);
    }

    public Point getLocal() {
        return local;
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
}
