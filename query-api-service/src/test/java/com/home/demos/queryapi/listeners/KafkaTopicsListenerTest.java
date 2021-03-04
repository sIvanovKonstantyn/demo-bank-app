package com.home.demos.queryapi.listeners;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.home.demos.queryapi.dto.DepositCommandResult;
import com.home.demos.queryapi.services.DepositApiService;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ExtendWith(MockitoExtension.class)
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9093", "port=9093"})
@ActiveProfiles({"kafka-test", "application-layer-test"})
class KafkaTopicsListenerTest {

    @Value(value = "${kafka.bootstrapAddress}")
    private String bootstrapAddress;

    @Mock
    private DepositApiService depositService;

    @Autowired
    private KafkaTopicsListener kafkaTopicsListener;

    private KafkaTemplate<String, String> kafkaTemplate;

    @BeforeEach
    void setupEach() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        ProducerFactory<String, String> producerFactory = new DefaultKafkaProducerFactory<>(configProps);

        kafkaTemplate = new KafkaTemplate<>(producerFactory);
        kafkaTopicsListener.setDepositService(depositService);
    }

    @Test
    void createDepositResultListener() throws JsonProcessingException {

        DepositCommandResult value = new DepositCommandResult();
        value.setRequestID("cd123");
        value.setResultCode(0);
        kafkaTemplate.send("createdDeposits", new ObjectMapper().writer().writeValueAsString(value));

        Awaitility.await().atMost(10, TimeUnit.SECONDS).until(kafkaTopicsListener::wasCreateSuccessfulCall);

        Mockito.verify(depositService, Mockito.times(1)).saveDepositCommandResult(
                Mockito.any()
        );

    }

    @Test
    void replenishDepositResultListener() throws JsonProcessingException {
        DepositCommandResult value = new DepositCommandResult();
        value.setRequestID("cd123");
        value.setResultCode(0);
        kafkaTemplate.send("changedDeposits", new ObjectMapper().writer().writeValueAsString(value));

        Awaitility.await().atMost(10, TimeUnit.SECONDS).until(kafkaTopicsListener::wasReplenishSuccessfulCall);

        Mockito.verify(depositService, Mockito.times(1)).saveDepositCommandResult(
                Mockito.any()
        );
    }

    @Test
    void repayDepositResultListener() throws JsonProcessingException {
        DepositCommandResult value = new DepositCommandResult();
        value.setRequestID("cd123");
        value.setResultCode(0);
        kafkaTemplate.send("removedDeposits", new ObjectMapper().writer().writeValueAsString(value));

        Awaitility.await().atMost(10, TimeUnit.SECONDS).until(kafkaTopicsListener::wasRepaySuccessfulCall);

        Mockito.verify(depositService, Mockito.times(1)).saveDepositCommandResult(
                Mockito.any()
        );
    }
}