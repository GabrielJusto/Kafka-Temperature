package br.com.bonatto.broker.consumer;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MainConsumer
{

    public static void main(String[] args)
    {
        AnnotationConfigApplicationContext appContext = new AnnotationConfigApplicationContext();
        appContext.scan("br.com.bonatto.broker");
        appContext.refresh();

        ClientRegisterConsumer clientRegisterConsumer = new ClientRegisterConsumer();
        clientRegisterConsumer.consume();
    }
}
