package com.home.demos.deposit.infrastructure;

import com.home.demos.deposit.domain.QueryAPINotificator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:application.yml")
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
