package com.sumerge.auth.producer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public abstract class AbstractProducer<T> {

    protected final KafkaTemplate<String, T> kafkaTemplate;

    protected AbstractProducer(KafkaTemplate<String, T> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    protected void sendMessage(T eventModel, String topic) {
        log.info(String.format("$$ -> Producing message --> %s, to topic: %s", eventModel, topic));
        this.kafkaTemplate.send(topic, eventModel);
    }

    protected void sendMessage(T eventModel, String topic, String key) {
        log.info(String.format("$$ -> Producing message --> %s, to topic: %s", eventModel, topic));
        this.kafkaTemplate.send(topic, key, eventModel);
    }
}