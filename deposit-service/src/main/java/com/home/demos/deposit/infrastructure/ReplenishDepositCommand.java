package com.home.demos.deposit.infrastructure;

import lombok.Data;

@Data
public class ReplenishDepositCommand {
    private String requestID;
    private Long depositID;
    private Long sum;
}
