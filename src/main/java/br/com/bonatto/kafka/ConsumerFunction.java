package br.com.bonatto.kafka;

import com.influxdb.client.InfluxDBClient;
import org.apache.kafka.clients.consumer.ConsumerRecord;

public interface ConsumerFunction<T> {
    void consume(ConsumerRecord<String, T> record, InfluxDBClient client, String bucket, String org);
}
