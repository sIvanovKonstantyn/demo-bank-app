package com.home.demos.queryapi.services;

import com.home.demos.queryapi.dto.DepositCommandResult;
import com.home.demos.queryapi.repositories.DepositCommandsResultsRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class DepositApiServiceTest {

    private static final String EXPECTED_ID = "expectedID";

    @Mock
    private DepositCommandsResultsRepository repository;

    @InjectMocks
    private DepositApiService service;

    @Test
    void takeCreatedDepositResultWhenDataWasFoundThenShouldBeExpectedIdAndErrorCodeInResponse() {
        Mockito.when(repository.findById(EXPECTED_ID)).thenReturn(
                Optional.of(new DepositCommandResult(EXPECTED_ID, 0))
        );

        DepositCommandResult depositCommandResult = service.takeCreatedDepositResult(EXPECTED_ID);
        assertEquals(Integer.valueOf(0), depositCommandResult.getResultCode());
        assertEquals(EXPECTED_ID, depositCommandResult.getRequestID());
    }

    @Test
    void takeCreatedDepositResultWhenNoDataFoundThenShouldBeExpectedIdAndErrorCodeInResponse() {
        DepositCommandResult depositCommandResult = service.takeCreatedDepositResult(EXPECTED_ID);
        assertEquals(Integer.valueOf(2), depositCommandResult.getResultCode());
        assertEquals(EXPECTED_ID, depositCommandResult.getRequestID());
    }

    @Test
    void saveDepositCommandResultWhenEverythingGoesRightThenRepositorySaveMethodShouldBeCalled() {
        service.saveDepositCommandResult(new DepositCommandResult());

        Mockito.verify(repository, Mockito.times(1)).save(Mockito.any());
    }
}