package com.home.demos.deposit.infrastructure;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.home.demos.deposit.application.services.DepositService;
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
    private DepositService depositService;

    @Autowired
    private ObjectMapper mapper;

    private boolean wasAnyCreateCall;
    private boolean wasAnyReplenishCall;
    private boolean wasAnyRepayCall;

    @KafkaListener(
            topics = "createDepositCommands",
            containerFactory = "createDepositCommandsKafkaListenerContainerFactory")
    public void createDepositCommandListener(String createDepositCommandString) throws JsonProcessingException {

        CreateDepositCommand createDepositCommand = mapper.readValue(
                createDepositCommandString,
                CreateDepositCommand.class
        );

        depositService.createDeposit(
                createDepositCommand.getRequestID(),
                createDepositCommand.getName(),
                createDepositCommand.getSum(),
                createDepositCommand.getCapitalizationType(),
                createDepositCommand.getCurrencyCode(),
                createDepositCommand.getDepositType(),
                createDepositCommand.getCloseDate(),
                createDepositCommand.getIncomeRate()
        );

        wasAnyCreateCall = true;
    }

    @KafkaListener(
            topics = "replenishDepositCommands",
            containerFactory = "replenishDepositCommandsKafkaListenerContainerFactory")
    public void replenishDepositCommandListener(String replenishDepositCommandString) throws JsonProcessingException {

        ReplenishDepositCommand replenishDepositCommand = mapper.readValue(
                replenishDepositCommandString,
                ReplenishDepositCommand.class
        );

        depositService.replenishDeposit(
                replenishDepositCommand.getRequestID(),
                replenishDepositCommand.getDepositID(),
                replenishDepositCommand.getSum()
        );

        wasAnyReplenishCall = true;
    }

    @KafkaListener(
            topics = "repayDepositCommands",
            containerFactory = "repayDepositCommandsKafkaListenerContainerFactory")
    public void repayDepositCommandListener(String repayDepositCommandString) throws JsonProcessingException {

        RepayDepositCommand repayDepositCommand = mapper.readValue(
                repayDepositCommandString,
                RepayDepositCommand.class
        );

        depositService.repayDeposit(
                repayDepositCommand.getRequestID(),
                repayDepositCommand.getDepositID()
        );

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