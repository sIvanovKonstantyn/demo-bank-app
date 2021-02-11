package com.home.demos.deposit.infrastructure;

public class RepayDepositCommand {
    private String requestID;
    private Long depositID;

    public String getRequestID() {
        return requestID;
    }

    public void setRequestID(String requestID) {
        this.requestID = requestID;
    }

    public Long getDepositID() {
        return depositID;
    }

    public void setDepositID(Long depositID) {
        this.depositID = depositID;
    }
}
