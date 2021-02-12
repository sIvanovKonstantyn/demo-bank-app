package com.home.demos.deposit.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class DepositTest {

    private Deposit deposit = new Deposit();

    @Test
    void hasSameInfo_whenInfoObjectsAreEqual_thenShouldBeSuccessfulResponse() {
        DepositInfo depositInfo = new DepositInfo(
                "new deposit",
                DepositState.OPENED,
                1L,
                "capitalizationType",
                840,
                "depositType",
                LocalDateTime.now(),
                1
        );

        Deposit deposit = this.deposit.create(depositInfo);
        assertTrue(deposit.hasSameInfo(depositInfo));
    }

    @Test
    void hasSameInfo_whenInfoObjectsAreNotqual_thenShouldBeFailedResponse() {
        DepositInfo depositInfo = new DepositInfo(
                "new deposit",
                DepositState.OPENED,
                1L,
                "capitalizationType",
                840,
                "depositType",
                LocalDateTime.now(),
                1
        );

        Deposit deposit = this.deposit.create(depositInfo);
        assertFalse(deposit.hasSameInfo(new DepositInfo()));
    }

    @Test
    void hasSameInfo_whenInputInfoObjectIsNull_thenShouldBeFailedResponse() {
        DepositInfo depositInfo = new DepositInfo(
                "new deposit",
                DepositState.OPENED,
                1L,
                "capitalizationType",
                840,
                "depositType",
                LocalDateTime.now(),
                1
        );

        Deposit deposit = this.deposit.create(depositInfo);
        assertFalse(deposit.hasSameInfo(null));
    }

    @Test
    void create_whenEverythingGoesRight_depositInfoShouldBeSet() {
        DepositInfo depositInfo = new DepositInfo(
                "new deposit",
                DepositState.OPENED,
                1L,
                "capitalizationType",
                840,
                "depositType",
                LocalDateTime.now(),
                1
        );

        Deposit deposit = this.deposit.create(depositInfo);
        assertTrue(deposit.hasSameInfo(depositInfo));
    }

    @Test
    void create_whenInfoIsEmpty_illegalArgumentShouldBeThrown() {

        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> this.deposit.create(new DepositInfo())
        );

        assertTrue(thrown.getMessage().contains("deposit info could not be empty"));
    }

    @Test
    void create_whenInfoIsNull_illegalArgumentShouldBeThrown() {

        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> this.deposit.create(null)
        );

        assertTrue(thrown.getMessage().contains("deposit info could not be empty"));
    }


    @Test
    void replenish_whenInsideDepositInfoIsNull_thenIllegalStateShouldBeThrown() {

        IllegalStateException thrown = assertThrows(
                IllegalStateException.class,
                () -> deposit.replenish(10L)
        );

        assertTrue(thrown.getMessage().contains("deposit info should be present"));
    }

    @Test
    void replenish_whenEverythingGoesRight_thenDepositSumShouldBeChanged() {

        DepositInfo depositInfo = new DepositInfo(
                "new deposit",
                DepositState.OPENED,
                1L,
                "capitalizationType",
                840,
                "depositType",
                LocalDateTime.now(),
                1
        );

        Deposit deposit = this.deposit.create(depositInfo);
        deposit.replenish(9L);

        assertEquals(Long.valueOf(10L), deposit.takeSum());
    }

    @Test
    void repay_whenInsideDepositInfoIsNull_thenIllegalStateShouldBeThrown() {

        IllegalStateException thrown = assertThrows(
                IllegalStateException.class,
                () -> deposit.repay()
        );

        assertTrue(thrown.getMessage().contains("deposit info should be present"));
    }

    @Test
    void repay_whenEverythingGoesRight_thenDepositSumShouldBeChanged() {

        DepositInfo depositInfo = new DepositInfo(
                "new deposit",
                DepositState.OPENED,
                1L,
                "capitalizationType",
                840,
                "depositType",
                LocalDateTime.now(),
                1
        );

        Deposit deposit = this.deposit.create(depositInfo);
        deposit.repay();

        assertTrue(deposit.isClosed());
    }
}