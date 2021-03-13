package com.home.demos.deposit.infrastructure;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.home.demos.deposit.application.services.DepositService;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
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
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
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
    private DepositService depositService;

    @Autowired
    private KafkaTopicsListener kafkaTopicsListener;

    private KafkaTemplate<String, String> kafkaTemplate;

    @BeforeEach
    void setupEach() throws Exception {
        Map<String, Object> consumerProps = new HashMap<>();
        consumerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, "some-group");

        Consumer<String, String> consumer = new KafkaConsumer<>(consumerProps);
        consumer.subscribe(Arrays.asList("repayDepositCommands", "createDepositCommands", "replenishDepositCommands"));

        consumer.poll(Duration.ofMillis(1000));


        Map<String, Object> configProps = new HashMap<>();

        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        ProducerFactory<String, String> producerFactory = new DefaultKafkaProducerFactory<>(configProps);

        System.out.println(" kafkaTopicsListener.setDepositService(depositService): " + depositService);
        kafkaTemplate = new KafkaTemplate<>(producerFactory);
        kafkaTopicsListener.setDepositService(depositService);
    }

    @Test
    void createDepositCommandListener() throws JsonProcessingException {

        CreateDepositCommand value = new CreateDepositCommand();
        value.setRequestID("cd123");
        value.setDepositType("type");
        value.setCloseDate(LocalDateTime.now());
        value.setIncomeRate(1);
        value.setSum(10L);
        value.setName("some deposit");
        value.setCurrencyCode(840);
        value.setCapitalizationType("type");

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        objectMapper.registerModule(new Jdk8Module())
                .registerModule(new JavaTimeModule());

        kafkaTemplate.send("createDepositCommands", objectMapper.writer().writeValueAsString(value));

        Awaitility.await().atMost(10, TimeUnit.SECONDS).until(kafkaTopicsListener::wasCreateSuccessfulCall);

        Mockito.verify(depositService).createDeposit(
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
        value.setRequestID("cd123");
        value.setDepositID(123L);
        value.setSum(10L);

        kafkaTemplate.send("replenishDepositCommands", new ObjectMapper().writer().writeValueAsString(value));

        Awaitility.await().atMost(10, TimeUnit.SECONDS).until(kafkaTopicsListener::wasReplenishSuccessfulCall);

        Mockito.verify(depositService).replenishDeposit(
                Mockito.any(),
                Mockito.any(),
                Mockito.any()
        );
    }

    @Test
    void repayDepositCommandListener() throws JsonProcessingException {
        RepayDepositCommand value = new RepayDepositCommand();
        value.setRequestID("cd" + UUID.randomUUID());
        value.setDepositID(123L);

        System.out.println("---sending..." + value.getRequestID());
        kafkaTemplate.send("repayDepositCommands", new ObjectMapper().writer().writeValueAsString(value));

        Awaitility.await().atMost(10, TimeUnit.SECONDS).until(kafkaTopicsListener::wasRepaySuccessfulCall);

        Mockito.verify(depositService).repayDeposit(
                Mockito.any(),
                Mockito.any()
        );
    }
}