package com.home.demos.deposit.infrastructure;

import java.time.LocalDateTime;

public class CreateDepositCommand {
   private String requestID;
   private String name;
   private Long sum;
   private String capitalizationType;
   private Integer currencyCode;
   private String depositType;
   private LocalDateTime closeDate;
   private Integer incomeRate;

    public String getRequestID() {
        return requestID;
    }

    public String getName() {
        return name;
    }

    public Long getSum() {
        return sum;
    }

    public String getCapitalizationType() {
        return capitalizationType;
    }

    public Integer getCurrencyCode() {
        return currencyCode;
    }

    public String getDepositType() {
        return depositType;
    }

    public LocalDateTime getCloseDate() {
        return closeDate;
    }

    public Integer getIncomeRate() {
        return incomeRate;
    }

    public void setRequestID(String requestID) {
        this.requestID = requestID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSum(Long sum) {
        this.sum = sum;
    }

    public void setCapitalizationType(String capitalizationType) {
        this.capitalizationType = capitalizationType;
    }

    public void setCurrencyCode(Integer currencyCode) {
        this.currencyCode = currencyCode;
    }

    public void setDepositType(String depositType) {
        this.depositType = depositType;
    }

    public void setCloseDate(LocalDateTime closeDate) {
        this.closeDate = closeDate;
    }

    public void setIncomeRate(Integer incomeRate) {
        this.incomeRate = incomeRate;
    }
}
