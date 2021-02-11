package com.home.demos.deposit.infrastructure;

import com.home.demos.deposit.domain.Deposit;

public class DepositRemovedMessage extends DepositMessage {
    public DepositRemovedMessage(String requestID, Deposit removedDeposit) {
        this.requestID = requestID;
        this.deposit = removedDeposit;
    }

    public DepositRemovedMessage() {
    }
}
