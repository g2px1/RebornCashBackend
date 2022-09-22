package com.client.eurekaclient.repositories;

import com.client.eurekaclient.models.web3.BlockchainData;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface BlockchainsRepository extends MongoRepository<BlockchainData, String> {
    Page<BlockchainData> findByPlatform(@NotNull String platform, Pageable pageable);
    Optional<BlockchainData> findByName(@NotNull String name);
    List<BlockchainData> findAll();
}
