package org.example.ecommerce;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.jetbrains.annotations.NotNull;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class NewOrderMain {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        var producer = new KafkaProducer<String, String>(properties());
        for(int i=0; i < 10; i++){
            var value = i +",616513213,1350";
            var record = new ProducerRecord<>("ECOMMERCE_NEW_ORDER", value, value);
            var email = "Thankss! We are processing your order, protocol["+ i +"] =)";
            var emailRecord = new ProducerRecord<>("ECOMMERCE_SEND_EMAIL", email, email);

            producer.send(record, getCallback()).get();
            producer.send(emailRecord, getCallback()).get();

        }

    }

    @NotNull
    private static Callback getCallback() {
        return (data, ex) -> {
            if(ex != null){
                ex.printStackTrace();
                return;
            }
            System.out.println("sucesso enviado " + data.topic() + ":::partition " + data.partition() + "/ offset "
                    + data.offset() + " / timestamp " + data.timestamp());
        };
    }


    private static Properties properties(){
        var properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.0.122:9092");
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        return properties;
    }
}
