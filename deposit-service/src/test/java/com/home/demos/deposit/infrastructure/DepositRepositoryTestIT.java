package com.home.demos.deposit.infrastructure;

import com.home.demos.deposit.domain.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Testcontainers
@SpringBootTest
@ActiveProfiles("test")
class DepositRepositoryTestIT {

    @Container
    public static PostgreSQLContainer postgreSQLContainer = TestPostgresqlContainer.getInstance();

    @Autowired
    private DepositRepository depositRepository;

    @AfterAll
    static void shutdownAll() {
        postgreSQLContainer.stop();
    }

    @Test
    void save_whenSavingWasSuccessful_thenSavedDepositShouldNotBeEmpty() {
        DepositInfo depositInfo = new DepositInfo(
                "test deposit",
                DepositState.OPENED,
                1L,
                "add to deposit",
                840,
                "unclosed",
                LocalDateTime.of(2100, 1, 1, 0, 0),
                10
        );

        Deposit deposit = new Deposit().create(depositInfo);
        Deposit savedDeposit = depositRepository.save(deposit);

        Assertions.assertFalse(savedDeposit.isEmpty());
    }

    @Test
    void save_whenSaveExistingDepositWithNewInfo_thenItsInfoShouldBeUpdated() {
        DepositInfo depositInfo = new DepositInfo(
                "test deposit",
                DepositState.OPENED,
                1L,
                "add to deposit",
                840,
                "unclosed",
                LocalDateTime.of(2100, 1, 1, 0, 0),
                10
        );

        Deposit initialDeposit = new Deposit().create(depositInfo);
        Deposit savedDeposit = depositRepository.save(initialDeposit);

        savedDeposit.replenish(200L);
        Deposit changedDeposit = depositRepository.save(savedDeposit);

        Assertions.assertNotEquals(initialDeposit, changedDeposit);
    }

    @Test
    void findById_whenDataIsPresentInDB_thenDepositShouldBeFound() {

        DepositID id = new DepositID(System.currentTimeMillis());

        DepositInfo depositInfo = new DepositInfo(
                "test deposit",
                DepositState.OPENED,
                1L,
                "add to deposit",
                840,
                "unclosed",
                LocalDateTime.of(2100, 1, 1, 0, 0),
                10
        );

        Deposit deposit = new Deposit().create(depositInfo, id);
        depositRepository.save(deposit);

        Optional<Deposit> foundDeposit = depositRepository.findById(id);

        Assertions.assertTrue(foundDeposit.isPresent());
    }

    @Test
    void findById_whenDataIsNotPresentInDB_thenDepositShouldNotBeFound() {
        DepositID id = new DepositID(System.currentTimeMillis());

        Optional<Deposit> foundDeposit = depositRepository.findById(id);

        Assertions.assertFalse(foundDeposit.isPresent());
    }

    @Test
    void findAll_whenDataIsPresentInDB_thenShouldBeSomeItemsInResponse() {
        DepositInfo depositInfo = new DepositInfo(
                "test deposit",
                DepositState.OPENED,
                1L,
                "add to deposit",
                840,
                "unclosed",
                LocalDateTime.of(2100, 1, 1, 0, 0),
                10
        );

        Deposit deposit = new Deposit().create(depositInfo);
        depositRepository.save(deposit);

        List<Deposit> items = depositRepository.findAll();

        Assertions.assertFalse(items.isEmpty());
    }

    @Test
    void findAll_whenDataIsNotPresentInDB_thenShouldBeEmptyListAsResponse() {
        depositRepository.deleteAll();

        List<Deposit> items = depositRepository.findAll();

        Assertions.assertTrue(items.isEmpty());
    }
}