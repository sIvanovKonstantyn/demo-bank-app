package com.home.demos.queryapi.repositories;

import com.home.demos.queryapi.dto.DepositCommandResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@DataMongoTest
@ExtendWith(SpringExtension.class)
class CreatedDepositResultsRepositoryTest {

    @Autowired
    private DepositCommandsResultsRepository repository;

    @Test
    public void saveAndGetByIdOperationsShouldWorkCorrectly() {

        DepositCommandResult commandResult = new DepositCommandResult();
        commandResult.setRequestID("abc123");
        commandResult.setResultCode(0);

        repository.save(commandResult);
        DepositCommandResult foundResult = repository.findById(commandResult.getRequestID()).get();

        Assertions.assertEquals(commandResult.getRequestID(), foundResult.getRequestID());
        Assertions.assertEquals(commandResult.getResultCode(), foundResult.getResultCode());
    }
}