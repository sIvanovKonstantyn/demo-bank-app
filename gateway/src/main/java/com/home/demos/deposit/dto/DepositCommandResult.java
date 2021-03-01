package com.home.demos.deposit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepositCommandResult {
    private String requestID;
    private Integer resultCode;
}
