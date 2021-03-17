package com.home.demos.deposit.controller;

import com.home.demos.deposit.dto.*;
import com.home.demos.deposit.service.DepositService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/deposit")
public class DepositController {

    @Autowired
    private DepositService service;

    @PostMapping("/create")
    public DepositServiceResult create(@RequestBody Deposit deposit) {
        return service.create(deposit);
    }

    @PostMapping("/replenish")
    public DepositReplenishmentServiceResult replenish(@RequestBody DepositReplenishment depositReplenishment) {
        return service.replenish(depositReplenishment);
    }

    @PostMapping("/repay")
    public DepositRepaymentServiceResult repay(@RequestBody DepositRepayment depositRepayment) {
        return service.repay(depositRepayment);
    }
}
