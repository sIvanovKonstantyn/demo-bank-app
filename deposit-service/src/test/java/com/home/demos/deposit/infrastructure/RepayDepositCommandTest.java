package com.home.demos.deposit.infrastructure;

import com.home.demos.deposit.domain.Deposit;
import com.home.demos.deposit.domain.DepositID;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RepayDepositCommandTest {

    private static final String REQUEST_ID = "1";
    private static final long DEPOSIT_ID = 1L;

    private final RepayDepositCommand repayDepositCommand = new RepayDepositCommand();

    @Test
    void allLombokFeaturesShouldWork() {
        repayDepositCommand.setRequestID(REQUEST_ID);
        repayDepositCommand.setDepositID(DEPOSIT_ID);

        assertEquals(REQUEST_ID, repayDepositCommand.getRequestID());
        assertEquals(DEPOSIT_ID, repayDepositCommand.getDepositID());
        assertNotNull(repayDepositCommand.toString());
        assertTrue(repayDepositCommand.canEqual(new RepayDepositCommand()));
        assertNotEquals(new RepayDepositCommand(), repayDepositCommand);
        assertNotEquals(0, repayDepositCommand.hashCode());
    }
}