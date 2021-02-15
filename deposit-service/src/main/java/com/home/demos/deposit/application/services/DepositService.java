package com.home.demos.deposit.application.services;

import com.home.demos.deposit.application.factories.DepositFactory;
import com.home.demos.deposit.domain.Deposit;
import com.home.demos.deposit.domain.DepositID;
import com.home.demos.deposit.domain.DepositRepository;
import com.home.demos.deposit.domain.QueryAPINotificator;
import com.home.demos.deposit.infrastructure.DepositChangedMessage;
import com.home.demos.deposit.infrastructure.DepositCreatedMessage;
import com.home.demos.deposit.infrastructure.DepositRemovedMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Profile({"main","application-layer-test"})
public class DepositService {
    @Autowired
    private DepositFactory depositFactory;

    @Autowired
    private DepositRepository depositRepository;

    @Autowired
    private QueryAPINotificator queryAPINotificator;

    public void createDeposit(
            String requestID,
            String name,
            Long sum,
            String capitalizationType,
            Integer currencyCode,
            String depositType,
            LocalDateTime closeDate,
            Integer incomeRate
    ) {

        final Deposit createdDeposit = depositFactory.createNewDeposit(
                name,
                sum,
                capitalizationType,
                currencyCode,
                depositType,
                closeDate,
                incomeRate
        );

        final Deposit savedDeposit = depositRepository.save(createdDeposit);

        queryAPINotificator.notify(new DepositCreatedMessage(requestID, savedDeposit));
    }

    public void replenishDeposit(String requestID, Long depositID, Long sum) {

        final Deposit foundDeposit = depositRepository.findById(new DepositID(depositID))
                .orElseThrow(() -> new IllegalArgumentException(String.format("deposit not found by id %s", depositID)));

        foundDeposit.replenish(sum);

        final Deposit changedDeposit = depositRepository.save(foundDeposit);

        queryAPINotificator.notify(new DepositChangedMessage(requestID, changedDeposit));
    }

    public void repayDeposit(String requestID, Long depositID) {
        final Deposit foundDeposit = depositRepository.findById(new DepositID(depositID))
                .orElseThrow(() -> new IllegalArgumentException(String.format("deposit not found by id %s", depositID)));

        foundDeposit.repay();

        final Deposit removedDeposit = depositRepository.save(foundDeposit);

        queryAPINotificator.notify(new DepositRemovedMessage(requestID, removedDeposit));
    }
}
