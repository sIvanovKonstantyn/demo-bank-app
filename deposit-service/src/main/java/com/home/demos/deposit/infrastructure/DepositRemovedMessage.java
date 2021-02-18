package com.home.demos.deposit.infrastructure;

import com.home.demos.deposit.domain.Deposit;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class DepositRemovedMessage extends DepositMessage {
    public DepositRemovedMessage(String requestID, Deposit deposit) {
        super(requestID, deposit);
    }
}
