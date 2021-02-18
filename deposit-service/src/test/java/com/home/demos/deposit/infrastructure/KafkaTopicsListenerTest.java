package com.home.demos.deposit.infrastructure;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.home.demos.deposit.application.services.DepositService;
import org.apache.kafka.clients.producer.ProducerConfig;
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
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@SpringBootTest
@DirtiesContext
@ExtendWith(MockitoExtension.class)
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9093", "port=9093"})
@ActiveProfiles({"kafka-test", "application-layer-test"})
class KafkaTopicsListenerTest {

    @Value(value = "${kafka.bootstrapAddress}")
    private String bootstrapAddress;

    @Mock
    private DepositService depositService;

    @Autowired
    private KafkaTopicsListener kafkaTopicsListener;

    private KafkaTemplate<String, String> kafkaTemplate;

    @BeforeEach
    void setupEach() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        ProducerFactory<String, String> producerFactory = new DefaultKafkaProducerFactory<>(configProps);

        kafkaTemplate = new KafkaTemplate<>(producerFactory);
        kafkaTopicsListener.setDepositService(depositService);
    }

    @Test
    void createDepositCommandListener() throws JsonProcessingException {

        CreateDepositCommand value = new CreateDepositCommand();
        value.setRequestID("cd123");
        kafkaTemplate.send("createDepositCommands", new ObjectMapper().writer().writeValueAsString(value));

        Awaitility.await().atMost(10, TimeUnit.SECONDS).until(kafkaTopicsListener::wasCreateSuccessfulCall);

        Mockito.verify(depositService, Mockito.times(1)).createDeposit(
                Mockito.any(),
                Mockito.any(),
                Mockito.any(),
                Mockito.any(),
                Mockito.any(),
                Mockito.any(),
                Mockito.any(),
                Mockito.any()
        );

    }

    @Test
    void replenishDepositCommandListener() throws JsonProcessingException {
        ReplenishDepositCommand value = new ReplenishDepositCommand();
        value.setRequestID("rpld123");
        kafkaTemplate.send("replenishDepositCommands", new ObjectMapper().writer().writeValueAsString(value));

        Awaitility.await().atMost(10, TimeUnit.SECONDS).until(kafkaTopicsListener::wasReplenishSuccessfulCall);

        Mockito.verify(depositService, Mockito.times(1)).replenishDeposit(
                Mockito.any(),
                Mockito.any(),
                Mockito.any()
        );
    }

    @Test
    void repayDepositCommandListener() throws JsonProcessingException {
        RepayDepositCommand value = new RepayDepositCommand();
        value.setRequestID("rpyd123");
        kafkaTemplate.send("repayDepositCommands", new ObjectMapper().writer().writeValueAsString(value));

        Awaitility.await().atMost(10, TimeUnit.SECONDS).until(kafkaTopicsListener::wasRepaySuccessfulCall);

        Mockito.verify(depositService, Mockito.times(1)).repayDeposit(
                Mockito.any(),
                Mockito.any()
        );
    }
}