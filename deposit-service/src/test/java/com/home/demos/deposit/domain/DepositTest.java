package com.home.demos.deposit.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class DepositTest {

    private static final String DEPOSIT_TYPE = "depositType";
    private static final String NEW_DEPOSIT = "new deposit";
    private static final String CAPITALIZATION_TYPE = "capitalizationType";
    private static final long SUM = 1L;
    private static final LocalDateTime CLOSE_DATE = LocalDateTime.now();
    private static final int CURRENCY_CODE = 840;
    private static final int INCOME_RATE = 1;

    private final Deposit deposit = new Deposit();

    @Test
    void hasSameInfoWhenInfoObjectsAreEqualThenShouldBeSuccessfulResponse() {
        DepositInfo depositInfo = new DepositInfo(
                NEW_DEPOSIT,
                DepositState.OPENED,
                SUM,
                CAPITALIZATION_TYPE,
                CURRENCY_CODE,
                DEPOSIT_TYPE,
                CLOSE_DATE,
                INCOME_RATE
        );

        Deposit createdDeposit = this.deposit.create(depositInfo);
        assertTrue(createdDeposit.hasSameInfo(depositInfo));
    }

    @Test
    void hasSameInfoWhenInfoObjectsAreNotEqualThenShouldBeFailedResponse() {
        DepositInfo depositInfo = new DepositInfo(
                NEW_DEPOSIT,
                DepositState.OPENED,
                SUM,
                CAPITALIZATION_TYPE,
                CURRENCY_CODE,
                DEPOSIT_TYPE,
                CLOSE_DATE,
                INCOME_RATE
        );

        Deposit createdDeposit = this.deposit.create(depositInfo);
        assertFalse(createdDeposit.hasSameInfo(new DepositInfo()));
    }

    @Test
    void hasSameInfoWhenInputInfoObjectIsNullThenShouldBeFailedResponse() {
        DepositInfo depositInfo = new DepositInfo(
                NEW_DEPOSIT,
                DepositState.OPENED,
                SUM,
                CAPITALIZATION_TYPE,
                CURRENCY_CODE,
                DEPOSIT_TYPE,
                CLOSE_DATE,
                INCOME_RATE
        );

        Deposit createdDeposit = this.deposit.create(depositInfo);
        assertFalse(createdDeposit.hasSameInfo(null));
    }

    @Test
    void createWhenEverythingGoesRightDepositInfoShouldBeSet() {
        DepositInfo depositInfo = new DepositInfo(
                NEW_DEPOSIT,
                DepositState.OPENED,
                SUM,
                CAPITALIZATION_TYPE,
                CURRENCY_CODE,
                DEPOSIT_TYPE,
                CLOSE_DATE,
                INCOME_RATE
        );

        Deposit createdDeposit = this.deposit.create(depositInfo);
        assertTrue(createdDeposit.hasSameInfo(depositInfo));
    }

    @Test
    void createWhenInfoIsEmptyIllegalArgumentShouldBeThrown() {

        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> this.deposit.create(new DepositInfo())
        );

        assertTrue(thrown.getMessage().contains("deposit info could not be empty"));
    }

    @Test
    void createWhenInfoIsNullIllegalArgumentShouldBeThrown() {

        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> this.deposit.create(null)
        );

        assertTrue(thrown.getMessage().contains("deposit info could not be empty"));
    }


    @Test
    void replenishWhenInsideDepositInfoIsNullThenIllegalStateShouldBeThrown() {

        IllegalStateException thrown = assertThrows(
                IllegalStateException.class,
                () -> deposit.replenish(10L)
        );

        assertTrue(thrown.getMessage().contains("deposit info should be present"));
    }

    @Test
    void replenishWhenEverythingGoesRightThenDepositSumShouldBeChanged() {

        DepositInfo depositInfo = new DepositInfo(
                NEW_DEPOSIT,
                DepositState.OPENED,
                SUM,
                CAPITALIZATION_TYPE,
                CURRENCY_CODE,
                DEPOSIT_TYPE,
                CLOSE_DATE,
                INCOME_RATE
        );

        Deposit createdDeposit = this.deposit.create(depositInfo);
        createdDeposit.replenish(9L);

        assertEquals(Long.valueOf(10L), createdDeposit.takeSum());
    }

    @Test
    void repayWhenInsideDepositInfoIsNullThenIllegalStateShouldBeThrown() {

        IllegalStateException thrown = assertThrows(IllegalStateException.class, deposit::repay);

        assertTrue(thrown.getMessage().contains("deposit info should be present"));
    }

    @Test
    void repayWhenEverythingGoesRightThenDepositSumShouldBeChanged() {

        DepositInfo depositInfo = new DepositInfo(
                NEW_DEPOSIT,
                DepositState.OPENED,
                SUM,
                CAPITALIZATION_TYPE,
                CURRENCY_CODE,
                DEPOSIT_TYPE,
                CLOSE_DATE,
                INCOME_RATE
        );

        Deposit createdDeposit = this.deposit.create(depositInfo);
        createdDeposit.repay();

        assertTrue(createdDeposit.isClosed());
    }
}