package com.home.demos.deposit.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepositRepaymentServiceResult {
    private DepositRepayment depositRepayment;
    private Integer resultCode;
}
