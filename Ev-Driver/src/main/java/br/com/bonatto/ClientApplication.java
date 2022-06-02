package br.com.bonatto;


import br.com.bonatto.producer.ClientService;

public class ClientApplication
{

    public static void main(String[] args) {

        ClientService service = new ClientService();
        service.run();
    }




}
