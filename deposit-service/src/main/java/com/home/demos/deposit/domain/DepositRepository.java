package com.home.demos.deposit.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface DepositRepository extends JpaRepository<Deposit, DepositID> {
}
