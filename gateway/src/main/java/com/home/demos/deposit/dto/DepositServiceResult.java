package com.home.demos.deposit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepositServiceResult {
    private Deposit deposit;
    private Integer resultCode;
}
