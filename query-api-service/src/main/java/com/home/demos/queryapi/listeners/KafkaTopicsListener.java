package com.home.demos.queryapi.listeners;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.home.demos.queryapi.dto.DepositCommandResult;
import com.home.demos.queryapi.services.DepositApiService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Profile({"main", "kafka-test"})
public class KafkaTopicsListener {

    @Autowired
    @Setter
    private DepositApiService depositService;

    @Autowired
    private ObjectMapper mapper;

    private boolean wasAnyCreateCall;
    private boolean wasAnyReplenishCall;
    private boolean wasAnyRepayCall;

    @KafkaListener(
            topics = "createdDeposits",
            containerFactory = "depositsCommandsResultsKafkaListenerContainerFactory")
    public void createdDepositsCommandListener(String createdDepositString) throws JsonProcessingException {

        DepositCommandResult createDepositResult = mapper.readValue(
                createdDepositString,
                DepositCommandResult.class
        );

        depositService.saveDepositCommandResult(createDepositResult);

        wasAnyCreateCall = true;
    }

    @KafkaListener(
            topics = "changedDeposits",
            containerFactory = "replenishDepositCommandsKafkaListenerContainerFactory")
    public void changedDepositsCommandListener(String changedDepositString) throws JsonProcessingException {

        DepositCommandResult changeDepositResult = mapper.readValue(
                changedDepositString,
                DepositCommandResult.class
        );

        depositService.saveDepositCommandResult(changeDepositResult);

        wasAnyReplenishCall = true;
    }

    @KafkaListener(
            topics = "removedDeposits",
            containerFactory = "repayDepositCommandsKafkaListenerContainerFactory")
    public void removedDepositsCommandListener(String removedDepositString) throws JsonProcessingException {

        DepositCommandResult changeDepositResult = mapper.readValue(
                removedDepositString,
                DepositCommandResult.class
        );

        depositService.saveDepositCommandResult(changeDepositResult);

        wasAnyRepayCall = true;
    }

    public Boolean wasCreateSuccessfulCall() {
        return wasAnyCreateCall;
    }

    public Boolean wasReplenishSuccessfulCall() {
        return wasAnyReplenishCall;
    }

    public Boolean wasRepaySuccessfulCall() {
        return wasAnyRepayCall;
    }
}