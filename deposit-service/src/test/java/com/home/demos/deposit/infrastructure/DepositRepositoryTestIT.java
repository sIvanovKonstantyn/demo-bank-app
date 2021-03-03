package com.home.demos.deposit.infrastructure;

import com.home.demos.deposit.domain.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@DirtiesContext
@ExtendWith(SpringExtension.class)
@SpringBootTest("db-test")
class DepositRepositoryTestIT {

    private static final String TEST_DEPOSIT = "test deposit";
    private static final long SUM = 1L;
    private static final String ADD_TO_DEPOSIT = "add to deposit";
    private static final String UNCLOSED = "unclosed";
    private static final LocalDateTime CLOSE_DATE = LocalDateTime.of(2100, 1, 1, 0, 0);
    private static final int CURRENCY_CODE = 840;
    private static final int INCOME_RATE = 10;

    @Autowired
    private DepositRepository depositRepository;

    @Test
    void saveWhenSavingWasSuccessfulThenSavedDepositShouldNotBeEmpty() {
        DepositInfo depositInfo = new DepositInfo(
                TEST_DEPOSIT,
                DepositState.OPENED,
                SUM,
                ADD_TO_DEPOSIT,
                CURRENCY_CODE,
                UNCLOSED,
                CLOSE_DATE,
                INCOME_RATE
        );

        Deposit createdDeposit = new Deposit().create(depositInfo);
        Deposit savedDeposit = depositRepository.save(createdDeposit);

        Assertions.assertFalse(savedDeposit.isEmpty());
    }

    @Test
    void saveWhenSaveExistingDepositWithNewInfoThenItsInfoShouldBeUpdated() {
        DepositInfo depositInfo = new DepositInfo(
                TEST_DEPOSIT,
                DepositState.OPENED,
                SUM,
                ADD_TO_DEPOSIT,
                CURRENCY_CODE,
                UNCLOSED,
                CLOSE_DATE,
                INCOME_RATE
        );

        Deposit initialDeposit = new Deposit().create(depositInfo);
        Deposit savedDeposit = depositRepository.save(initialDeposit);

        savedDeposit.replenish(200L);
        Deposit changedDeposit = depositRepository.save(savedDeposit);

        Assertions.assertNotEquals(initialDeposit, changedDeposit);
    }

    @Test
    void findByIdWhenDataIsPresentInDBThenDepositShouldBeFound() {

        DepositID id = new DepositID(System.currentTimeMillis());

        DepositInfo depositInfo = new DepositInfo(
                TEST_DEPOSIT,
                DepositState.OPENED,
                SUM,
                ADD_TO_DEPOSIT,
                CURRENCY_CODE,
                UNCLOSED,
                CLOSE_DATE,
                INCOME_RATE
        );

        Deposit deposit = new Deposit().create(depositInfo, id);
        depositRepository.save(deposit);

        Optional<Deposit> foundDeposit = depositRepository.findById(id);

        Assertions.assertTrue(foundDeposit.isPresent());
    }

    @Test
    void findByIdWhenDataIsNotPresentInDBThenDepositShouldNotBeFound() {
        DepositID id = new DepositID(System.currentTimeMillis());

        Optional<Deposit> foundDeposit = depositRepository.findById(id);

        Assertions.assertFalse(foundDeposit.isPresent());
    }

    @Test
    void findAllWhenDataIsPresentInDBThenShouldBeSomeItemsInResponse() {
        DepositInfo depositInfo = new DepositInfo(
                TEST_DEPOSIT,
                DepositState.OPENED,
                SUM,
                ADD_TO_DEPOSIT,
                CURRENCY_CODE,
                UNCLOSED,
                CLOSE_DATE,
                INCOME_RATE
        );

        Deposit createdDeposit = new Deposit().create(depositInfo);
        depositRepository.save(createdDeposit);

        List<Deposit> items = depositRepository.findAll();

        Assertions.assertFalse(items.isEmpty());
    }

    @Test
    void findAllWhenDataIsNotPresentInDBThenShouldBeEmptyListAsResponse() {
        depositRepository.deleteAll();

        List<Deposit> items = depositRepository.findAll();

        Assertions.assertTrue(items.isEmpty());
    }
}