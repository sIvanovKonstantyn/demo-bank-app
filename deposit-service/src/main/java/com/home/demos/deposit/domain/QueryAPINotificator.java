package com.home.demos.deposit.domain;

import com.home.demos.deposit.infrastructure.DepositChangedMessage;
import com.home.demos.deposit.infrastructure.DepositCreatedMessage;
import com.home.demos.deposit.infrastructure.DepositRemovedMessage;

public interface QueryAPINotificator {
    void notify(DepositCreatedMessage message);

    void notify(DepositChangedMessage message);

    void notify(DepositRemovedMessage message);
}
