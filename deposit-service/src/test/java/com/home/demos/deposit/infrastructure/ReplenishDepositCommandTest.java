package com.home.demos.deposit.infrastructure;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReplenishDepositCommandTest {

    private static final String REQUEST_ID = "1";
    private static final long DEPOSIT_ID = 1L;
    private static final long SUM = 1L;

    private final ReplenishDepositCommand replenishDepositCommand = new ReplenishDepositCommand();


    @Test
    void allLombokFeaturesShouldWork() {
        replenishDepositCommand.setRequestID(REQUEST_ID);
        replenishDepositCommand.setDepositID(DEPOSIT_ID);
        replenishDepositCommand.setSum(SUM);
        assertEquals(REQUEST_ID, replenishDepositCommand.getRequestID());
        assertEquals(DEPOSIT_ID, replenishDepositCommand.getDepositID());
        assertEquals(SUM, replenishDepositCommand.getSum());
        assertNotNull(replenishDepositCommand.toString());
        assertTrue(replenishDepositCommand.canEqual(new ReplenishDepositCommand()));
        assertNotEquals(new ReplenishDepositCommand(), replenishDepositCommand);
        assertNotEquals(0, replenishDepositCommand.hashCode());
    }
}