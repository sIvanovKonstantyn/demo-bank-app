package com.home.demos.deposit.infrastructure;

import com.home.demos.deposit.domain.Deposit;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9093", "port=9093"})
@ActiveProfiles({"kafka-test", "application-layer-test"})
class QueryAPINotificatorKafkaImplTestIT {

    @Value(value = "${kafka.bootstrapAddress}")
    private String bootstrapAddress;

    @Value(value = "${message.topic.created-deposits.name}")
    private String createdDepositsTopicName;

    @Value(value = "${message.topic.changed-deposits.name}")
    private String changedDepositsTopicName;

    @Value(value = "${message.topic.removed-deposits.name}")
    private String removedDepositsTopicName;

    @Autowired
    private QueryAPINotificatorKafkaImpl queryAPINotificatorKafka;

    private KafkaConsumer<String, DepositMessage> consumer;

    @BeforeEach
    void setupEach() {
        Map<String, Object> properties = new HashMap<>();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "tc-" + UUID.randomUUID());
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        JsonDeserializer<String> stringJsonDeserializer = new JsonDeserializer<>();
        stringJsonDeserializer.addTrustedPackages("com.home.demos.deposit.infrastructure");

        JsonDeserializer<DepositMessage> depositMessageJsonDeserializer = new JsonDeserializer<>();
        depositMessageJsonDeserializer.addTrustedPackages("com.home.demos.deposit.infrastructure");


        consumer = new KafkaConsumer<>(
                properties,
                stringJsonDeserializer,
                depositMessageJsonDeserializer
        );

        consumer.subscribe(Arrays.asList(
                createdDepositsTopicName,
                changedDepositsTopicName,
                removedDepositsTopicName
        ));
    }

    @Test
    void notifyWhenAllMessageTypesWasSentThenAllMessagesShouldBeReceivedByConsumer() {

        DepositCreatedMessage createdMessage = new DepositCreatedMessage("createdMessage".concat(UUID.randomUUID().toString()), new Deposit());
        DepositChangedMessage changedMessage = new DepositChangedMessage("changedMessage".concat(UUID.randomUUID().toString()), new Deposit());
        DepositRemovedMessage removedMessage = new DepositRemovedMessage("removedMessage".concat(UUID.randomUUID().toString()), new Deposit());

        queryAPINotificatorKafka.notify(createdMessage);
        queryAPINotificatorKafka.notify(changedMessage);
        queryAPINotificatorKafka.notify(removedMessage);

        ConsumerRecords<String, DepositMessage> polledData = consumer.poll(Duration.ofMillis(1000));

        List<DepositMessage> data = polledData.partitions().stream()
                .flatMap(topicPartition -> polledData.records(topicPartition).stream())
                .map(ConsumerRecord::value)
                .collect(Collectors.toList());

        Assertions.assertTrue(data.containsAll(Arrays.asList(createdMessage, changedMessage, removedMessage)));
    }
}