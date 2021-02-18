package com.home.demos.deposit.infrastructure;

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
}
