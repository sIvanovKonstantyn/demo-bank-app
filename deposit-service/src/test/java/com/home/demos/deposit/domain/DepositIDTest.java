package com.home.demos.deposit.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DepositIDTest {

    private final DepositID depositID = new DepositID();

    @Test
    void allLombokFeaturesShouldWork() {
        depositID.setId(1L);

        assertEquals(Long.valueOf(1L), depositID.getId());
        assertNotNull(depositID.toString());
        assertFalse(depositID.canEqual(new Deposit()));
        assertNotEquals(new DepositID(), depositID);
        assertNotEquals(0, depositID.hashCode());
    }
}