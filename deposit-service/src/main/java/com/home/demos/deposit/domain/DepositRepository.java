package com.home.demos.deposit.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DepositRepository extends JpaRepository<Deposit, DepositID> {
}
