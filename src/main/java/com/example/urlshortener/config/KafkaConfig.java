package com.example.urlshortener.config;

import com.example.urlshortener.model.ClickEntry;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

@Configuration
@Profile("prod")
public class KafkaConfig {
    private final KafkaProperties kafkaProperties;
    private final ObjectMapper objectMapper;

    public KafkaConfig(KafkaProperties kafkaProperties,
                       ObjectMapper objectMapper) {

        this.kafkaProperties = kafkaProperties;
        this.objectMapper = objectMapper;
    }

    @Bean
    public NewTopic clicksTopic() {
        return TopicBuilder
                .name("url-shortener.clicks")
                .partitions(3)
                .replicas(2)
                .build();
    }

    @Bean
    public ProducerFactory<String, ClickEntry> clickProducerFactory() {
        return new DefaultKafkaProducerFactory<>(
                kafkaProperties.buildProducerProperties(),
                new StringSerializer(),
                new JsonSerializer<>(objectMapper)
        );
    }

    @Bean
    public KafkaTemplate<String, ClickEntry> kafkaTemplate() {
        KafkaTemplate<String, ClickEntry> kafkaTemplate = new KafkaTemplate<>(clickProducerFactory());
        kafkaTemplate.setDefaultTopic("url-shortener.clicks");
        return kafkaTemplate;
    }

}
