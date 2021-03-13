package com.home.demos.deposit.application.services;

import com.home.demos.deposit.application.factories.DepositFactory;
import com.home.demos.deposit.domain.*;
import com.home.demos.deposit.infrastructure.DepositChangedMessage;
import com.home.demos.deposit.infrastructure.DepositCreatedMessage;
import com.home.demos.deposit.infrastructure.DepositRemovedMessage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class DepositServiceTest {

    @Mock
    private DepositFactory depositFactory;

    @Mock
    private DepositRepository depositRepository;

    @Mock
    private QueryAPINotificator queryAPINotificator;

    @InjectMocks
    private DepositService depositService;

    @BeforeEach
    void setUp() {

        Mockito.lenient().when(depositFactory.createNewDeposit(
                Mockito.any(),
                Mockito.any(),
                Mockito.any(),
                Mockito.any(),
                Mockito.any(),
                Mockito.any(),
                Mockito.any()
        )).thenReturn(new Deposit());

        Mockito.lenient().when(depositRepository.save(Mockito.any())).thenReturn(new Deposit());
        Mockito.lenient().when(depositRepository.findById(Mockito.any())).thenReturn(Optional.of(new Deposit().create(new DepositInfo(
                "new deposit",
                DepositState.OPENED,
                1L,
                "capitalizationType",
                840,
                "depositType",
                LocalDateTime.now(),
                1
        ))));
    }

    @Test
    void createDepositWhenCreateDepositCalledThenAllDependedMethodsShouldBeCalled() {
        depositService.createDeposit(
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );

        Mockito.verify(depositFactory, Mockito.times(1)).createNewDeposit(
                Mockito.any(),
                Mockito.any(),
                Mockito.any(),
                Mockito.any(),
                Mockito.any(),
                Mockito.any(),
                Mockito.any()
        );

        Mockito.verify(depositRepository, Mockito.times(1)).save(
                Mockito.any()
        );

        Mockito.verify(queryAPINotificator, Mockito.times(1)).notify(
                Mockito.any(DepositCreatedMessage.class)
        );
    }

    @Test
    void replenishDepositWhenReplenishDepositCalledThenAllDependedMethodsShouldBeCalled() {
        depositService.replenishDeposit(
                null,
                null,
                10L
        );

        Mockito.verify(depositRepository, Mockito.times(1)).findById(
                Mockito.any()
        );

        Mockito.verify(depositRepository, Mockito.times(1)).save(
                Mockito.any()
        );

        Mockito.verify(queryAPINotificator, Mockito.times(1)).notify(
                Mockito.any(DepositChangedMessage.class)
        );
    }

    @Test
    void replenishDepositWhenReplenishDepositCalledWithWrongIdThenNotificatorShouldBeCalled() {
        Mockito.lenient().when(depositRepository.findById(Mockito.any())).thenReturn(Optional.empty());

        depositService.replenishDeposit(null,null,null);

        Mockito.verify(queryAPINotificator, Mockito.times(1)).notify(
                Mockito.any(DepositChangedMessage.class)
        );
    }

    @Test
    void repayDepositWhenRepayDepositCalledThenNotificatorShouldBeCalled() {
        Mockito.lenient().when(depositRepository.findById(Mockito.any())).thenReturn(Optional.empty());

        depositService.repayDeposit(null,null);

        Mockito.verify(queryAPINotificator, Mockito.times(1)).notify(
                Mockito.any(DepositRemovedMessage.class)
        );
    }

    @Test
    void repayDepositWhenRepayDepositCalledWithWrongIdAllDependedMethodsShouldBeCalled() {
        depositService.repayDeposit(null, null);

        Mockito.verify(depositRepository, Mockito.times(1)).findById(
                Mockito.any()
        );

        Mockito.verify(depositRepository, Mockito.times(1)).save(
                Mockito.any()
        );

        Mockito.verify(queryAPINotificator, Mockito.times(1)).notify(
                Mockito.any(DepositRemovedMessage.class)
        );
    }
}