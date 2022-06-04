package br.com.bonatto.app;

import br.com.bonatto.consumer.KafkaConsumer;

import java.sql.SQLException;

public class AppConsumer
{
    public static void main(String[] args) throws SQLException {

        KafkaConsumer consumer = new KafkaConsumer();
        consumer.consume();
    }
}
