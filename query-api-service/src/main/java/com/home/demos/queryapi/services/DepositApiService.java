package com.home.demos.queryapi.services;

import com.home.demos.queryapi.dto.DepositCommandResult;
import com.home.demos.queryapi.repositories.DepositCommandsResultsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class DepositApiService {

    @Autowired
    private DepositCommandsResultsRepository repository;

    public DepositCommandResult takeCreatedDepositResult(String requestId) {
        System.out.printf("%s: find result by ID: %s%n", LocalDateTime.now(), requestId);

        DepositCommandResult foundResult = repository.findById(requestId).orElse(
                new DepositCommandResult(requestId, 2)
        );

        System.out.printf("%s: found result: %s%n", LocalDateTime.now(), foundResult);

        return foundResult;
    }

    public void saveDepositCommandResult(DepositCommandResult createDepositResult) {
        createDepositResult.setResultCode(0);
        repository.save(createDepositResult);
        System.out.printf("%s: deposit result saved to repo: %s%n", LocalDateTime.now(), createDepositResult);
    }
}
