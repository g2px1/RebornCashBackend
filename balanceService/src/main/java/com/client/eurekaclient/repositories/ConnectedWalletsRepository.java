package com.client.eurekaclient.repositories;

import com.client.eurekaclient.models.web3.ConnectedWallet;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ConnectedWalletsRepository extends MongoRepository<ConnectedWallet, String> {
    ConnectedWallet findByUsername(String username);
    ConnectedWallet findByAddress(String address);
}
