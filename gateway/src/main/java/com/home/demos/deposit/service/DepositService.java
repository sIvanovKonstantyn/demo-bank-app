package com.home.demos.deposit.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.home.demos.deposit.dto.*;
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

    public DepositServiceResult create(Deposit deposit) {
        System.out.printf("%s: gateway called with: %s%n", LocalDateTime.now(), deposit);

        CreateDepositCommand createDepositCommand = createDepositCommand(deposit);

        sendCreateDepositCommand(createDepositCommand);

        DepositCommandResult depositCommandResult = takeDepositCommandResult(createDepositCommand);

        return new DepositServiceResult(deposit, depositCommandResult.getResultCode());
    }

    private DepositCommandResult takeDepositCommandResult(DepositCommand depositCommand) {

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<DepositCommandResult> task = executor.submit(
                () -> {
                    DepositCommandResult depositCommandResult;
                    while (true) {
                        depositCommandResult = takeCommandResult(depositCommand.getRequestID());
                        if (depositCommandResult.getResultCode().equals(0)) {
                            break;
                        }
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            break;
                        }
                    }

                    return depositCommandResult;
                }

        );

        DepositCommandResult result;

        try {
            result = task.get(10, TimeUnit.SECONDS);
            System.out.printf("%s: deposit command result taken: %s%n", LocalDateTime.now(), result);
        } catch (Exception e) {
            result = new DepositCommandResult(depositCommand.getRequestID(), 2);
            System.out.printf("%s: deposit command result taken: TIMEOUT%n", LocalDateTime.now());
        } finally {
            task.cancel(true);
        }

        return result;
    }

    private void sendCreateDepositCommand(CreateDepositCommand createDepositCommand) {
        try {
            String dataAsString = makeJsonString(createDepositCommand);
            depositTopics.send("createDepositCommands", dataAsString);
            System.out.printf("%s: message sent to createDepositCommands: %s%n", LocalDateTime.now(), dataAsString);
        } catch (JsonProcessingException e) {
            System.out.printf("%s: message to createDepositCommands sending error: %s%n", LocalDateTime.now(), e.getMessage());
        }
    }

    private void sendReplenishDepositCommand(ReplenishDepositCommand replenishDepositCommand) {
        try {
            String dataAsString = makeJsonString(replenishDepositCommand);
            depositTopics.send("replenishDepositCommands", dataAsString);
            System.out.printf("%s: message sent to replenishDepositCommands: %s%n", LocalDateTime.now(), dataAsString);
        } catch (JsonProcessingException e) {
            System.out.printf("%s: message to replenishDepositCommands sending error: %s%n", LocalDateTime.now(), e.getMessage());
        }
    }

    private <T> String makeJsonString(T depositCommand) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new ParameterNamesModule())
                .registerModule(new Jdk8Module())
                .registerModule(new JavaTimeModule());

        return objectMapper.writer().writeValueAsString(depositCommand);
    }

    private CreateDepositCommand createDepositCommand(Deposit deposit) {
        String requestID = UUID.randomUUID().toString();

        return new CreateDepositCommand(requestID, deposit);
    }

    private DepositCommandResult takeCommandResult(String requestID) {
        try {
            System.out.printf("%s: find result for ID %s in url:%s%n", LocalDateTime.now(), requestID, commandResultsUrl);
            DepositCommandResult foundResult = restTemplate.getForObject(
                    commandResultsUrl.concat("/").concat(requestID),
                    DepositCommandResult.class
            );
            System.out.printf("%s: found result: %s%n", LocalDateTime.now(), foundResult);

            return foundResult;
        } catch (Exception e) {
            return new DepositCommandResult(requestID, 3);
        }
    }

    public DepositReplenishmentServiceResult replenish(DepositReplenishment depositReplenishment) {
        System.out.printf("%s: gateway called with: %s%n", LocalDateTime.now(), depositReplenishment);

        ReplenishDepositCommand replenishDepositCommand = replenishDepositCommand(depositReplenishment);

        sendReplenishDepositCommand(replenishDepositCommand);

        DepositCommandResult depositCommandResult = takeDepositCommandResult(replenishDepositCommand);

        return new DepositReplenishmentServiceResult(depositReplenishment, depositCommandResult.getResultCode());
    }

    private ReplenishDepositCommand replenishDepositCommand(DepositReplenishment depositReplenishment) {
        String requestID = UUID.randomUUID().toString();

        return new ReplenishDepositCommand(requestID, depositReplenishment);
    }


    public DepositRepaymentServiceResult repay(DepositRepayment depositRepayment) {
        System.out.printf("%s: gateway called with: %s%n", LocalDateTime.now(), depositRepayment);

        RepayDepositCommand replenishDepositCommand = repayDepositCommand(depositRepayment);

        sendRepaymentDepositCommand(replenishDepositCommand);

        DepositCommandResult depositCommandResult = takeDepositCommandResult(replenishDepositCommand);

        return new DepositRepaymentServiceResult(depositRepayment, depositCommandResult.getResultCode());
    }

    private RepayDepositCommand repayDepositCommand(DepositRepayment depositRepayment) {
        String requestID = UUID.randomUUID().toString();

        return new RepayDepositCommand(requestID, depositRepayment);
    }

    private void sendRepaymentDepositCommand(RepayDepositCommand repayDepositCommand) {
        try {
            String dataAsString = makeJsonString(repayDepositCommand);
            depositTopics.send("repayDepositCommands", dataAsString);
            System.out.printf("%s: message sent to repayDepositCommands: %s%n", LocalDateTime.now(), dataAsString);
        } catch (JsonProcessingException e) {
            System.out.printf("%s: message to repayDepositCommands sending error: %s%n", LocalDateTime.now(), e.getMessage());
        }
    }
}
