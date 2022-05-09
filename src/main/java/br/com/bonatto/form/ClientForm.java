package br.com.bonatto.form;

import br.com.bonatto.modelo.Client;
import br.com.bonatto.modelo.Point;

public class ClientForm
{

    private double lat;
    private double lon;
    private String connector;
    private double maxPrice;
    private String walletKey;

    public Client convert()
    {
        return new Client(new Point(lat, lon), connector, maxPrice, walletKey);
    }
}
