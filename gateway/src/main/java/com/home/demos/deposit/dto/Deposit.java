package com.home.demos.deposit.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Deposit {
    private String name;
    private Long sum;
    private String capitalizationType;
    private Integer currencyCode;
    private String depositType;
    private LocalDateTime closeDate;
    private Integer incomeRate;
}
