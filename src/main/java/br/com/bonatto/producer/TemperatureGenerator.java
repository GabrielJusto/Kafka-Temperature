package br.com.bonatto.producer;

import br.com.bonatto.kafka.KafkaDispatcher;

import java.util.Random;
import java.util.concurrent.ExecutionException;

public class TemperatureGenerator {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        try (var temperatureDispatcher = new KafkaDispatcher<Double>()) {
            for (var i = 0; i < 100; i++) {

                Random r = new Random();
                Double temperature = (r.nextDouble() * 80)-40;
                System.out.println(temperature);
                temperatureDispatcher.send("TEMPERATURE", temperature.toString(), temperature);
            }
        }

    }

}
