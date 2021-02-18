package com.home.demos.deposit.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDateTime;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepositInfo implements Serializable {
    private String name;
    private DepositState state;
    private Long sum;
    private String capitalizationType;
    private Integer currencyCode;
    private String depositType;
    private LocalDateTime closeDate;
    private Integer incomeRate;

    void addToSum(Long additionalSum) {
        if (this.sum == null) {
            this.sum = 0L;
        }

        this.sum += additionalSum;
    }

    void closeDeposit() {
        this.state = DepositState.CLOSED;
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
