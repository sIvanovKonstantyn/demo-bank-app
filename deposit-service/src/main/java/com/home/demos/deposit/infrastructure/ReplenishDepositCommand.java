package com.home.demos.deposit.infrastructure;

public class ReplenishDepositCommand {
    private String requestID;
    private Long depositID;
    private Long sum;

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

    public Long getSum() {
        return sum;
    }

    public void setSum(Long sum) {
        this.sum = sum;
    }
}
