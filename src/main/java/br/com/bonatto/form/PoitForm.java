package br.com.bonatto.form;

import br.com.bonatto.modelo.Point;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class PoitForm
{
    @NotEmpty @NotNull
    private double lat;
    @NotEmpty @NotNull
    private double lon;

    public Point convert()
    {
        return new Point(lat, lon);
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }
}
