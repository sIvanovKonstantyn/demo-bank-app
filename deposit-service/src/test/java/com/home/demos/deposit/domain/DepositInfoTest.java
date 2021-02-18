package com.home.demos.deposit.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class DepositInfoTest {

    private static final String NAME = "name";
    private static final long SUM = 1L;
    private static final String TYPE = "type";
    private static final int CURRENCY_CODE = 840;
    private static final LocalDateTime CLOSE_DATE = LocalDateTime.now();
    private static final int INCOME_RATE = 1;

    private final DepositInfo depositInfo = new DepositInfo();

    @Test
    void allLombokFeaturesShouldWork() {
        depositInfo.setName(NAME);
        depositInfo.setState(DepositState.OPENED);
        depositInfo.setSum(SUM);
        depositInfo.setCapitalizationType(TYPE);
        depositInfo.setCurrencyCode(CURRENCY_CODE);
        depositInfo.setDepositType(TYPE);
        depositInfo.setCloseDate(CLOSE_DATE);
        depositInfo.setIncomeRate(INCOME_RATE);

        assertEquals(NAME, depositInfo.getName());
        assertEquals(DepositState.OPENED, depositInfo.getState());
        assertEquals(SUM, depositInfo.getSum());
        assertEquals(TYPE, depositInfo.getCapitalizationType());
        assertEquals(CURRENCY_CODE, depositInfo.getCurrencyCode());
        assertEquals(TYPE, depositInfo.getDepositType());
        assertEquals(CLOSE_DATE, depositInfo.getCloseDate());
        assertEquals(INCOME_RATE, depositInfo.getIncomeRate());


        assertNotNull(depositInfo.toString());
        assertTrue(depositInfo.canEqual(new DepositInfo()));
        assertNotEquals(new DepositInfo(), depositInfo);
        assertNotEquals(0, depositInfo.hashCode());
    }
}