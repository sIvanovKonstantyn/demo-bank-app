package com.home.demos.deposit.infrastructure;

import com.home.demos.deposit.domain.Deposit;
import com.home.demos.deposit.infrastructure.configuration.KafkaProducerConfiguration;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.*;
import org.rnorth.ducttape.unreliables.Unreliables;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.google.common.collect.ImmutableMap;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Testcontainers
@SpringBootTest
@ActiveProfiles("test")
class QueryAPINotificatorKafkaImplTestIT {

    private static final String TOPIC_NAME = "testTopic";

    @Container
    public static PostgreSQLContainer postgreSQLContainer = TestPostgresqlContainer.getInstance();

    @Container
    public static KafkaContainer kafkaContainer = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:5.4.3"));

    private static String bootstrapServers;

    private QueryAPINotificatorKafkaImpl queryAPINotificatorKafka;
    private KafkaConsumer<String, DepositMessage> consumer;

    @BeforeAll
    static void setupAll() throws InterruptedException, ExecutionException, TimeoutException {
        kafkaContainer.start();
        bootstrapServers = kafkaContainer.getBootstrapServers();

        AdminClient adminClient = AdminClient.create(ImmutableMap.of(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers));
        Collection<NewTopic> topics = Collections.singletonList(new NewTopic(TOPIC_NAME, 1, (short) 1));

        adminClient.createTopics(topics).all().get(30, TimeUnit.SECONDS);
    }

    @AfterAll
    static void shutdownAll() {
        kafkaContainer.stop();
        postgreSQLContainer.stop();
    }

    @BeforeEach
    void setupEach() {
        KafkaTemplate<String, DepositMessage> kafkaTemplate = new KafkaTemplate<>(new KafkaProducerConfiguration().takeProducerFactory(bootstrapServers));


        queryAPINotificatorKafka = new QueryAPINotificatorKafkaImpl(
                TOPIC_NAME,
                TOPIC_NAME,
                TOPIC_NAME,
                kafkaTemplate
        );

        JsonDeserializer<String> stringJsonDeserializer = new JsonDeserializer<>();
        stringJsonDeserializer.addTrustedPackages("com.home.demos.deposit.infrastructure");

        JsonDeserializer<DepositMessage> depositMessageJsonDeserializer = new JsonDeserializer<>();
        depositMessageJsonDeserializer.addTrustedPackages("com.home.demos.deposit.infrastructure");

        consumer = new KafkaConsumer<>(
                ImmutableMap.of(
                        ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers,
                        ConsumerConfig.GROUP_ID_CONFIG, "tc-" + UUID.randomUUID(),
                        ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest"
                ),
                stringJsonDeserializer,
                depositMessageJsonDeserializer
        );

        consumer.subscribe(Collections.singletonList(TOPIC_NAME));
    }

    @AfterEach
    void shutdownEach() {
        consumer.unsubscribe();
    }

    @Test
    void notify_WhenAllMessageTypesWasSent_thenAllMessagesShouldBeReceivedByConsumer() {

        DepositCreatedMessage createdMessage = new DepositCreatedMessage("createdMessage", new Deposit());
        DepositChangedMessage changedMessage = new DepositChangedMessage("changedMessage", new Deposit());
        DepositRemovedMessage removedMessage = new DepositRemovedMessage("removedMessage", new Deposit());

        queryAPINotificatorKafka.notify(createdMessage);
        queryAPINotificatorKafka.notify(changedMessage);
        queryAPINotificatorKafka.notify(removedMessage);

        Unreliables.retryUntilTrue(10, TimeUnit.SECONDS, () -> {
            ConsumerRecords<String, DepositMessage> records = consumer.poll(Duration.ofMillis(100));

            if (records.isEmpty()) {
                return false;
            }

            Assertions.assertThat(records)
                    .hasSize(3)
                    .extracting(ConsumerRecord::topic, ConsumerRecord::key, ConsumerRecord::value)
                    .containsAll(
                            Arrays.asList(
                                    Tuple.tuple(TOPIC_NAME, null, createdMessage),
                                    Tuple.tuple(TOPIC_NAME, null, changedMessage),
                                    Tuple.tuple(TOPIC_NAME, null, removedMessage)
                            )
                    );

            return true;
        });
    }
}