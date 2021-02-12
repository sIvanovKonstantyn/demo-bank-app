package com.home.demos.deposit.application.factories;

import com.home.demos.deposit.domain.Deposit;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class DepositFactoryTest {

    private final DepositFactory depositFactory = new DepositFactory();

    @Test
    void createNewDeposit() {
        Deposit createdDeposit = depositFactory.createNewDeposit(
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );

        assertTrue(createdDeposit.isOpen());
    }
}