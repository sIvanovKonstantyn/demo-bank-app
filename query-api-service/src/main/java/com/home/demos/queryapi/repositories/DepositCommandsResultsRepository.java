package com.home.demos.queryapi.repositories;

import com.home.demos.queryapi.dto.DepositCommandResult;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DepositCommandsResultsRepository extends MongoRepository<DepositCommandResult, String> {
}
