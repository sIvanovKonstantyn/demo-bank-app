package com.home.demos.deposit.domain;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Embeddable
public class DepositInfo implements Serializable {
    private String name;
    private DepositState state;
    private Long sum;
    private String capitalizationType;
    private Integer currencyCode;
    private String depositType;
    private LocalDateTime closeDate;
    private Integer incomeRate;

    public DepositInfo(
            String name,
            DepositState state,
            Long sum,
            String capitalizationType,
            Integer currencyCode,
            String depositType,
            LocalDateTime closeDate,
            Integer incomeRate
    ) {
        this.name = name;
        this.state = state;
        this.sum = sum;
        this.capitalizationType = capitalizationType;
        this.currencyCode = currencyCode;
        this.depositType = depositType;
        this.closeDate = closeDate;
        this.incomeRate = incomeRate;
    }

    public DepositInfo() {
    }

    void addToSum(Long additionalSum) {
        if (this.sum == null) {
            this.sum = 0L;
        }

        this.sum += additionalSum;
    }

    void closeDeposit() {
        this.state = DepositState.CLOSED;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DepositState getState() {
        return state;
    }

    public void setState(DepositState state) {
        this.state = state;
    }

    public Long getSum() {
        return sum;
    }

    public void setSum(Long sum) {
        this.sum = sum;
    }

    public String getCapitalizationType() {
        return capitalizationType;
    }

    public void setCapitalizationType(String capitalizationType) {
        this.capitalizationType = capitalizationType;
    }

    public Integer getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(Integer currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getDepositType() {
        return depositType;
    }

    public void setDepositType(String depositType) {
        this.depositType = depositType;
    }

    public LocalDateTime getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(LocalDateTime closeDate) {
        this.closeDate = closeDate;
    }

    public Integer getIncomeRate() {
        return incomeRate;
    }

    public void setIncomeRate(Integer incomeRate) {
        this.incomeRate = incomeRate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DepositInfo that = (DepositInfo) o;
        return Objects.equals(name, that.name) && state == that.state && Objects.equals(sum, that.sum) && Objects.equals(capitalizationType, that.capitalizationType) && Objects.equals(currencyCode, that.currencyCode) && Objects.equals(depositType, that.depositType) && Objects.equals(closeDate, that.closeDate) && Objects.equals(incomeRate, that.incomeRate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, state, sum, capitalizationType, currencyCode, depositType, closeDate, incomeRate);
    }

    public boolean isEmpty() {
        return name == null
                || name.isEmpty()
                || state == null
                || sum == null
                || capitalizationType == null
                || capitalizationType.isEmpty()
                || currencyCode == null
                || depositType == null
                || depositType.isEmpty()
                || closeDate == null
                || incomeRate == null;
    }
}
