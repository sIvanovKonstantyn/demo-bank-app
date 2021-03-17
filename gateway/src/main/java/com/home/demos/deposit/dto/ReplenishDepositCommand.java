package com.home.demos.deposit.dto;

import lombok.Data;

@Data
public class ReplenishDepositCommand implements DepositCommand {
    private String requestID;
    private Long depositID;
    private Long sum;

    public ReplenishDepositCommand(String requestID, DepositReplenishment deposit) {
        this.requestID = requestID;
        this.depositID = deposit.getDepositID();
        this.sum = deposit.getSum();
    }
}
