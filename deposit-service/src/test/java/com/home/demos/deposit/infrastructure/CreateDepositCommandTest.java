package com.home.demos.deposit.infrastructure;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class CreateDepositCommandTest {

    private static final String REQUEST_ID = "1";
    private static final String NAME = "name";
    private static final long SUM = 1L;
    private static final String TYPE = "type";
    private static final int CURRENCY_CODE = 840;
    private static final LocalDateTime CLOSE_DATE = LocalDateTime.now();
    private static final int INCOME_RATE = 1;

    private final CreateDepositCommand createDepositCommand = new CreateDepositCommand();

    @Test
    void allLombokFeaturesShouldWork() {
        createDepositCommand.setRequestID(REQUEST_ID);
        createDepositCommand.setName(NAME);
        createDepositCommand.setSum(SUM);
        createDepositCommand.setCapitalizationType(TYPE);
        createDepositCommand.setCurrencyCode(CURRENCY_CODE);
        createDepositCommand.setDepositType(TYPE);
        createDepositCommand.setCloseDate(CLOSE_DATE);
        createDepositCommand.setIncomeRate(INCOME_RATE);

        Assertions.assertEquals(REQUEST_ID, createDepositCommand.getRequestID());
        Assertions.assertEquals(NAME, createDepositCommand.getName());
        Assertions.assertEquals(SUM, createDepositCommand.getSum());
        Assertions.assertEquals(TYPE, createDepositCommand.getCapitalizationType());
        Assertions.assertEquals(CURRENCY_CODE, createDepositCommand.getCurrencyCode());
        Assertions.assertEquals(TYPE, createDepositCommand.getDepositType());
        Assertions.assertEquals(CLOSE_DATE, createDepositCommand.getCloseDate());
        Assertions.assertEquals(INCOME_RATE, createDepositCommand.getIncomeRate());
        assertNotNull(createDepositCommand.toString());
        assertTrue(createDepositCommand.canEqual(new CreateDepositCommand()));
        assertNotEquals(new CreateDepositCommand(), createDepositCommand);
        assertNotEquals(0, createDepositCommand.hashCode());
    }
}