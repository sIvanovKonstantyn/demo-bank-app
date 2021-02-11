package com.home.demos.deposit.infrastructure.configuration;

import com.home.demos.deposit.application.services.DepositService;
import com.home.demos.deposit.infrastructure.CreateDepositCommand;
import com.home.demos.deposit.infrastructure.RepayDepositCommand;
import com.home.demos.deposit.infrastructure.ReplenishDepositCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;

@Configuration
@Profile("enable-infrastructure-layer")
public class KafkaConsumerConfiguration {

    @Autowired
    private DepositService depositService;

    @KafkaListener(
            topics = "createDepositCommands",
            containerFactory = "createDepositCommandsKafkaListenerContainerFactory")
    public void createDepositCommandListener(CreateDepositCommand createDepositCommand) {
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
    }

    @KafkaListener(
            topics = "replenishDepositCommands",
            containerFactory = "replenishDepositCommandsKafkaListenerContainerFactory")
    public void replenishDepositCommandListener(ReplenishDepositCommand replenishDepositCommand) {
        depositService.replenishDeposit(
                replenishDepositCommand.getRequestID(),
                replenishDepositCommand.getDepositID(),
                replenishDepositCommand.getSum()
        );
    }

    @KafkaListener(
            topics = "repayDepositCommands",
            containerFactory = "replenishDepositCommandsKafkaListenerContainerFactory")
    public void repayDepositCommandListener(RepayDepositCommand repayDepositCommand) {
        depositService.repayDeposit(
                repayDepositCommand.getRequestID(),
                repayDepositCommand.getDepositID()
        );
    }
}