package com.home.demos.deposit.controller;

import com.home.demos.deposit.dto.CreateDepositResult;
import com.home.demos.deposit.dto.Deposit;
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
    public CreateDepositResult create(@RequestBody Deposit deposit) {
        return service.create(deposit);
    }
}
