package com.home.demos.deposit.infrastructure.configuration;

import com.home.demos.deposit.application.services.DepositService;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@Profile({"main", "kafka-test"})
public class KafkaConsumerConfiguration {

    @Value(value = "${kafka.bootstrapAddress}")
    private String bootstrapAddress;

    @Autowired
    private DepositService depositService;

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String>
    createDepositCommandsKafkaListenerContainerFactory() {

        ConcurrentKafkaListenerContainerFactory<String, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory("createDepositCommands"));
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String>
    replenishDepositCommandsKafkaListenerContainerFactory() {

        ConcurrentKafkaListenerContainerFactory<String, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory("replenishDepositCommands"));
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String>
    repayDepositCommandsKafkaListenerContainerFactory() {

        ConcurrentKafkaListenerContainerFactory<String, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory("repayDepositCommands"));
        return factory;
    }

    @Bean
    public ConsumerFactory<String, String> consumerFactory(String topicName) {
        Map<String, Object> props = new HashMap<>();
        props.put(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                bootstrapAddress);
        props.put(
                ConsumerConfig.GROUP_ID_CONFIG,
                topicName);
        props.put(
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class);
        props.put(
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                JsonDeserializer.class);

        props.put(
                ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,
                "earliest");

        return new DefaultKafkaConsumerFactory<>(props);
    }

}