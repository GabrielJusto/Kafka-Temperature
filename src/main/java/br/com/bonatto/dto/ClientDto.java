package br.com.bonatto.dto;

import br.com.bonatto.modelo.Point;

public class ClientDto
{
    private PointDto local;
    private String connector;
    private double maxPrice;
    private String walletKey;


    public ClientDto(Point local, String connector, double maxPrice, String walletKey) {
        this.local = new PointDto(local);
        this.connector = connector;
        this.maxPrice = maxPrice;
        this.walletKey = walletKey;
    }
}
