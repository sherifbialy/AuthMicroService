package com.sumerge.auth.config;

import com.sumerge.auth.common.Constants;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class Topics {

    @Value("${kafka.number.of.partitions}")
    private int numPartitions;

    @Value("${kafka.number.of.replication}")
    private short replicationFactor;

    @Bean
    public NewTopic userTopic() {
        return new NewTopic(Constants.USER_DATA_TOPIC, numPartitions, replicationFactor);
    }

}
