package com.home.demos.deposit.domain;

import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import java.util.Objects;

@Entity
public class Deposit {

    @EmbeddedId
    private DepositID depositID = new DepositID();

    @Embedded
    private DepositInfo depositInfo;

    public Deposit create(DepositInfo depositInfo) {
        if (depositInfo == null || depositInfo.isEmpty()) {
            throw new IllegalArgumentException("deposit info could not be empty");
        }

        this.depositInfo = depositInfo;
        return this;
    }

    public Deposit create(DepositInfo depositInfo, DepositID depositID) {
        this.depositInfo = depositInfo;
        this.depositID = depositID;
        return this;
    }

    public void replenish(Long additionalSum) {
        if (depositInfo == null) {
            throw new IllegalStateException("deposit info should be present");
        }

        depositInfo.addToSum(additionalSum);
    }

    public void repay() {

        if (depositInfo == null) {
            throw new IllegalStateException("deposit info should be present");
        }

        depositInfo.closeDeposit();
    }

    public boolean isEmpty() {
        return depositID.getId() == null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Deposit deposit = (Deposit) o;
        return Objects.equals(depositID, deposit.depositID) && Objects.equals(depositInfo, deposit.depositInfo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(depositID, depositInfo);
    }

    public boolean isOpen() {
        if (depositInfo == null || depositInfo.getState() == null) {
            return false;
        }

        return DepositState.OPENED.equals(depositInfo.getState());
    }

    public boolean isClosed() {
        if (depositInfo == null || depositInfo.getState() == null) {
            return false;
        }

        return DepositState.CLOSED.equals(depositInfo.getState());
    }

    public boolean hasSameInfo(DepositInfo depositInfo) {
        if (depositInfo == null) {
            return false;
        }

        return depositInfo.equals(this.depositInfo);
    }

    public Long takeSum() {
        if (depositInfo == null) {
            return 0L;
        }

        return depositInfo.getSum();
    }
}
