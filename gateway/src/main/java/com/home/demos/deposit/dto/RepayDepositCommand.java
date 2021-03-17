package com.home.demos.deposit.dto;

import lombok.Data;

@Data
public class RepayDepositCommand implements DepositCommand {
    private String requestID;
    private Long depositID;

    public RepayDepositCommand(String requestID, DepositRepayment deposit) {
        this.requestID = requestID;
        this.depositID = deposit.getDepositID();
    }
}
