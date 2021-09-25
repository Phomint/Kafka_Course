package org.example.ecommerce;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.io.Closeable;
import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.UUID;
import java.util.regex.Pattern;

public class KafkaService implements Closeable {

    private final ConsumerFunction parse;
    private KafkaConsumer<String, String> consumer;

    KafkaService(String groupId, String topic, ConsumerFunction parse) {
        this(parse, groupId);
        consumer.subscribe(Collections.singletonList(topic));
    }

    public KafkaService(String groupId, Pattern topic, ConsumerFunction parse) {
        this(parse, groupId);
        consumer.subscribe(topic);
    }

    private KafkaService(ConsumerFunction parse, String groupId) {
        this.parse = parse;
        this.consumer =  new KafkaConsumer(properties(groupId));
    }

    void run() {
        while (true){
            var records = consumer.poll(Duration.ofMillis(100));
            if (!records.isEmpty()) {
                System.out.println("Encontrei " + records.count() +" Registros");
                for(var record: records) {
                    parse.consume(record);
                }

                System.out.println("Email Sent");
            }
        }
    }

    public static Properties properties(String groupId){
        var properties = new Properties();

        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.0.122:9092");
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        properties.setProperty(ConsumerConfig.CLIENT_ID_CONFIG, UUID.randomUUID().toString());

        return properties;
    }

    @Override
    public void close() {
        consumer.close();
    }
}
