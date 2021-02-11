package com.home.demos.deposit.infrastructure;

import com.home.demos.deposit.domain.Deposit;

public class DepositCreatedMessage extends DepositMessage {
    public DepositCreatedMessage(String requestID, Deposit createdDeposit) {
        this.requestID = requestID;
        this.deposit = createdDeposit;
    }

    public DepositCreatedMessage() {
    }
}
