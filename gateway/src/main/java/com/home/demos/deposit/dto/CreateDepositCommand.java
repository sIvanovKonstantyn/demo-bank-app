package com.home.demos.deposit.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateDepositCommand {
    private String requestID;
    private String name;
    private Long sum;
    private String capitalizationType;
    private Integer currencyCode;
    private String depositType;
    private LocalDateTime closeDate;
    private Integer incomeRate;

    public CreateDepositCommand(String requestID, Deposit deposit) {
        this.requestID = requestID;
        this.name = deposit.getName();
        this.sum = deposit.getSum();
        this.capitalizationType = deposit.getCapitalizationType();
        this.currencyCode = deposit.getCurrencyCode();
        this.depositType = deposit.getDepositType();
        this.closeDate = deposit.getCloseDate();
        this.incomeRate = deposit.getIncomeRate();
    }
}
