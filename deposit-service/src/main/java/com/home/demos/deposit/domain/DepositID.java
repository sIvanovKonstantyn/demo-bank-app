package com.home.demos.deposit.domain;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class DepositID implements Serializable {

    private Long id = System.currentTimeMillis();

    public DepositID(Long id) {
        this.id = id;
    }

    public DepositID() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DepositID depositID = (DepositID) o;
        return Objects.equals(id, depositID.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
