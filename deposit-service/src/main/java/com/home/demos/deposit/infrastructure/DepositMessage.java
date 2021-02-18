package com.home.demos.deposit.infrastructure;

import com.home.demos.deposit.domain.Deposit;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public abstract class DepositMessage {
    protected String requestID;
    protected Deposit deposit;

    public DepositMessage(String requestID, Deposit deposit) {
        this.requestID = requestID;
        this.deposit = deposit;
    }
}
