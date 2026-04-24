package com.example.demo.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public void send(String topic, Object data) {
    	System.out.println("I am In Send");
        kafkaTemplate.send(topic, data);
    }
}
