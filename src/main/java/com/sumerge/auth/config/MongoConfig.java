package com.sumerge.auth.config;

import com.sumerge.auth.repository.entity.UserDocument;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertCallback;

import java.util.UUID;

@Configuration
public class MongoConfig {


    @Bean
    public BeforeConvertCallback<UserDocument> beforeSaveCallback() {

        return (entity, collection) -> {

            if (entity.getId() == null) {
                entity.setId(UUID.randomUUID());
            }
            return entity;
        };
    }
}
