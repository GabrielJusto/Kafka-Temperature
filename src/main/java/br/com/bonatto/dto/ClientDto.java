package br.com.bonatto.dto;

import br.com.bonatto.modelo.Point;

public class ClientDto
{
    private int id;
    private Point local;
    private String connector;
    private double maxPrice;
    private String walletKey;


    public ClientDto(int id, Point local, String connector, double maxPrice, String walletKey) {
        this.id = id;
        this.local = local;
        this.connector = connector;
        this.maxPrice = maxPrice;
        this.walletKey = walletKey;
    }
}
