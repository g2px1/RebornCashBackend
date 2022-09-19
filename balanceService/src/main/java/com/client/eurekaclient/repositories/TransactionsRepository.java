package com.client.eurekaclient.repositories;

import com.client.eurekaclient.models.web3.Transaction;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TransactionsRepository extends MongoRepository<Transaction, String> {
    Page<Transaction> findByUsername(@NotNull String username, Pageable pageable);
    Page<Transaction> findByUsernameAndChainName(@NotNull String username, @NotNull String chainName, Pageable pageable);
    Transaction findByHashAndChainName(@NotNull String hash, @NotNull String chainName);
    boolean existsByHashAndChainName(String hash, String chainName);
}
