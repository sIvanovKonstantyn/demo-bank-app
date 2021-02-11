package com.home.demos.deposit.infrastructure;

import com.home.demos.deposit.domain.Deposit;

import java.util.Objects;

public abstract class DepositMessage {
    protected String requestID;
    protected Deposit deposit;

    public String getRequestID() {
        return requestID;
    }

    public void setRequestID(String requestID) {
        this.requestID = requestID;
    }

    public Deposit getDeposit() {
        return deposit;
    }

    public void setDeposit(Deposit deposit) {
        this.deposit = deposit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DepositMessage that = (DepositMessage) o;
        return Objects.equals(requestID, that.requestID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requestID);
    }
}
