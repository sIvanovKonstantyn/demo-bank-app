package com.home.demos.queryapi.services;

import com.home.demos.queryapi.dto.DepositCommandResult;
import com.home.demos.queryapi.repositories.DepositCommandsResultsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DepositApiService {

    @Autowired
    private DepositCommandsResultsRepository repository;

    public DepositCommandResult takeCreatedDepositResult(String requestId) {
        return repository.findById(requestId).orElse(
                new DepositCommandResult(requestId, 2)
        );
    }

    public void saveDepositCommandResult(DepositCommandResult createDepositResult) {
        createDepositResult.setResultCode(0);
        repository.save(createDepositResult);
    }
}
