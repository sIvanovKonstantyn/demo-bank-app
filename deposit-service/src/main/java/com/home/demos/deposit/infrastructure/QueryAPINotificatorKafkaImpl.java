package com.home.demos.deposit.infrastructure;

import com.home.demos.deposit.domain.QueryAPINotificator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Profile({"main", "kafka-test"})
public class QueryAPINotificatorKafkaImpl implements QueryAPINotificator {

    @Value(value = "${message.topic.created-deposits.name}")
    private String createdDepositsTopicName;

    @Value(value = "${message.topic.changed-deposits.name}")
    private String changedDepositsTopicName;

    @Value(value = "${message.topic.removed-deposits.name}")
    private String removedDepositsTopicName;

    @Autowired
    private KafkaTemplate<String, DepositMessage> kafkaTemplate;

    @Override
    public void notify(DepositCreatedMessage message) {
        kafkaTemplate.send(createdDepositsTopicName, message);
        System.out.printf("%s: deposit send to createdDeposits: %s%n", LocalDateTime.now(), message);
    }

    @Override
    public void notify(DepositChangedMessage message) {
        kafkaTemplate.send(changedDepositsTopicName, message);
    }

    @Override
    public void notify(DepositRemovedMessage message) {
        kafkaTemplate.send(removedDepositsTopicName, message);
    }
}
