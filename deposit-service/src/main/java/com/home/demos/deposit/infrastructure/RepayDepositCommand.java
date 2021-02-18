package com.home.demos.deposit.infrastructure;

import lombok.Data;

@Data
public class RepayDepositCommand {
    private String requestID;
    private Long depositID;
}
