package com.home.demos.deposit.application.factories;

import com.home.demos.deposit.domain.Deposit;
import com.home.demos.deposit.domain.DepositInfo;
import com.home.demos.deposit.domain.DepositState;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DepositFactory {
    public Deposit createNewDeposit(
            String name,
            Long sum,
            String capitalizationType,
            Integer currencyCode,
            String depositType,
            LocalDateTime closeDate,
            Integer incomeRate
    ) {

        DepositInfo depositInfo = new DepositInfo(
                name,
                DepositState.OPENED,
                sum,
                capitalizationType,
                currencyCode,
                depositType,
                closeDate,
                incomeRate
        );

        Deposit deposit = new Deposit();

        return deposit.create(depositInfo);

    }
}
