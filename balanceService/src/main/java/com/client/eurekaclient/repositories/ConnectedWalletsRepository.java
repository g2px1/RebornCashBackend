package com.client.eurekaclient.repositories;

import com.client.eurekaclient.models.web3.ConnectedWallet;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ConnectedWalletsRepository extends MongoRepository<ConnectedWallet, String> {
    Optional<ConnectedWallet> findByUsername(String username);
    Optional<ConnectedWallet> findByAddress(String address);
    boolean existsByUsername(String address);
    void deleteByUsername(String username);
}
