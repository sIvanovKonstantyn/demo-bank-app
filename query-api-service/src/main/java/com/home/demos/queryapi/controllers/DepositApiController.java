package com.home.demos.queryapi.controllers;

import com.home.demos.queryapi.dto.DepositCommandResult;
import com.home.demos.queryapi.services.DepositApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/deposit-request-api")
public class DepositApiController {

    @Autowired
    private DepositApiService service;

    @GetMapping("/take-command-result/{requestId}")
    public DepositCommandResult takeDepositCommandResult(@PathVariable("requestId") String requestId) {
        return service.takeCreatedDepositResult(requestId);
    }
}
