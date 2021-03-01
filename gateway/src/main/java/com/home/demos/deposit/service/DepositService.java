package com.home.demos.deposit.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.home.demos.deposit.dto.CreateDepositCommand;
import com.home.demos.deposit.dto.CreateDepositResult;
import com.home.demos.deposit.dto.Deposit;
import com.home.demos.deposit.dto.DepositCommandResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.*;

@Service
public class DepositService {

    @Autowired
    private KafkaTemplate<String, String> depositTopics;

    @Autowired
    private RestTemplate restTemplate;

    @Value(value = "${deposit.command-results.url}")
    private String commandResultsUrl;

    @Value(value = "${message.topic.created-deposits-commands.name}")
    private String createdDepositCommandTopicName;

    public CreateDepositResult create(Deposit deposit) {
        System.out.printf("%s: gateway called with: %s%n", LocalDateTime.now(), deposit);

        CreateDepositCommand createDepositCommand = createDepositCommand(deposit);

        sendDepositCommand(createDepositCommand);

        DepositCommandResult depositCommandResult = takeDepositCommandResult(createDepositCommand);

        return new CreateDepositResult(deposit, depositCommandResult.getResultCode());
    }

    private DepositCommandResult takeDepositCommandResult(CreateDepositCommand createDepositCommand) {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

        Future<DepositCommandResult> task = executor.submit(
                () -> takeCommandResultWhenItWillBePresent(createDepositCommand.getRequestID())
        );

        DepositCommandResult result;

        try {
            result = task.get(10, TimeUnit.SECONDS);
            System.out.printf("%s: deposit command result taken: %s%n", LocalDateTime.now(), result);
        } catch (Exception e) {
            result = new DepositCommandResult("", 2);
            System.out.printf("%s: deposit command result taken: TIMEOUT%n", LocalDateTime.now());
        }

        return result;
    }

    private void sendDepositCommand(CreateDepositCommand createDepositCommand) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new ParameterNamesModule())
                    .registerModule(new Jdk8Module())
                    .registerModule(new JavaTimeModule());

            String dataAsString = objectMapper.writer().writeValueAsString(createDepositCommand);
            depositTopics.send("createDepositCommands", dataAsString);
            System.out.printf("%s: message sent to createDepositCommands: %s%n", LocalDateTime.now(), dataAsString);
        } catch (JsonProcessingException e) {
            System.out.printf("%s: message to createDepositCommands sending error: %s%n", LocalDateTime.now(), e.getMessage());
        }
    }

    private CreateDepositCommand createDepositCommand(Deposit deposit) {
        String requestID = UUID.randomUUID().toString();

        return new CreateDepositCommand(requestID, deposit);
    }

    private DepositCommandResult takeCommandResultWhenItWillBePresent(String requestID) throws InterruptedException, ExecutionException {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

        ScheduledFuture<DepositCommandResult> scheduledFuture = createTakeResultTask(executor, requestID);

        while (true) {
            try {
                if (scheduledFuture.get().getResultCode() == 0) break;
            } catch (InterruptedException | ExecutionException e) {
                System.out.printf("%s: found ERROR: %s%n", LocalDateTime.now(), e.getMessage());
            }

            scheduledFuture = createTakeResultTask(executor, requestID);
        }

        return scheduledFuture.get();
    }

    private ScheduledFuture<DepositCommandResult> createTakeResultTask(ScheduledExecutorService executor, String requestID) {
        return executor.schedule(
                () -> takeCommandResult(requestID),
                1,
                TimeUnit.SECONDS
        );
    }

    private DepositCommandResult takeCommandResult(String requestID) {
        System.out.printf("%s: find result for ID %s in url:%s%n", LocalDateTime.now(), requestID, commandResultsUrl);
        DepositCommandResult foundResult = restTemplate.getForObject(
                commandResultsUrl.concat("/").concat(requestID),
                DepositCommandResult.class
        );
        System.out.printf("%s: found result: %s%n", LocalDateTime.now(), foundResult);

        return foundResult;
    }
}
