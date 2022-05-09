package br.com.bonatto.ev_driver.dto;

import br.com.bonatto.broker.dto.PointDto;
import br.com.bonatto.broker.model.Point;

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
