package br.com.bonatto.app;

import br.com.bonatto.consumer.KafkaConsumer;
import org.springframework.boot.autoconfigure.SpringBootApplication;

public class AppConsumer
{
    public static void main(String[] args) {

        KafkaConsumer consumer = new KafkaConsumer();
        consumer.consume();
    }
}
