package com.home.demos.deposit.infrastructure;

import com.home.demos.deposit.domain.Deposit;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class DepositChangedMessage extends DepositMessage {
    public DepositChangedMessage(String requestID, Deposit deposit) {
        super(requestID, deposit);
    }
}
