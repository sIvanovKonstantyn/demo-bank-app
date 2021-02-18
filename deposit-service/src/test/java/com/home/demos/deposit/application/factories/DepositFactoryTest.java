package com.home.demos.deposit.application.factories;

import com.home.demos.deposit.domain.Deposit;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertTrue;

class DepositFactoryTest {

    private final DepositFactory depositFactory = new DepositFactory();

    @Test
    void createNewDeposit() {
        Deposit createdDeposit = depositFactory.createNewDeposit(
                "new deposit",
                1L,
                "capitalizationType",
                840,
                "depositType",
                LocalDateTime.now(),
                1
        );

        assertTrue(createdDeposit.isOpen());
    }
}