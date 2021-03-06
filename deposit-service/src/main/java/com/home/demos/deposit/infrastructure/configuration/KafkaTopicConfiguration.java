package com.home.demos.deposit.infrastructure.configuration;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
@Profile({"main", "kafka-test"})
public class KafkaTopicConfiguration {

    @Value(value = "${kafka.bootstrapAddress}")
    private String bootstrapAddress;

    @Value(value = "${message.topic.created-deposits.name}")
    private String createdDepositsTopicName;

    @Value(value = "${message.topic.changed-deposits.name}")
    private String changedDepositsTopicName;

    @Value(value = "${message.topic.removed-deposits.name}")
    private String removedDepositsTopicName;

    @Value(value = "${message.topic.create-deposit-commands.name}")
    private String createDepositCommandsTopicName;

    @Value(value = "${message.topic.replenish-deposit-commands.name}")
    private String replenishDepositCommandsTopicName;

    @Value(value = "${message.topic.repay-deposit-commands.name}")
    private String repayDepositCommandsTopicName;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic createdDepositsTopic() {
        return new NewTopic(createdDepositsTopicName, 1, (short) 1);
    }

    @Bean
    public NewTopic changedDepositsTopic() {
        return new NewTopic(changedDepositsTopicName, 1, (short) 1);
    }

    @Bean
    public NewTopic removedDepositsTopic() {
        return new NewTopic(removedDepositsTopicName, 1, (short) 1);
    }

    @Bean
    public NewTopic createDepositCommandsTopic() {
        return new NewTopic(createDepositCommandsTopicName, 1, (short) 1);
    }
    @Bean
    public NewTopic replenishDepositCommandsTopic() {
        return new NewTopic(replenishDepositCommandsTopicName, 1, (short) 1);
    }
    @Bean
    public NewTopic repayDepositCommandsTopic() {
        return new NewTopic(repayDepositCommandsTopicName, 1, (short) 1);
    }
}