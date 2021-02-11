package com.home.demos.deposit.infrastructure;

import com.home.demos.deposit.domain.Deposit;

public class DepositChangedMessage extends DepositMessage {

    public DepositChangedMessage(String requestID, Deposit createdDeposit) {
        this.requestID = requestID;
        this.deposit = createdDeposit;
    }

    public DepositChangedMessage() {
    }
}
