package com.client.eurekaclient.repositories;

import com.client.eurekaclient.models.web3.BlockchainData;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface BlockchainsRepository extends MongoRepository<BlockchainData, String> {
//    Page<BlockchainData> findAll(@NotNull Pageable pageable);
    Page<BlockchainData> findByPlatform(@NotNull String platform, Pageable pageable);
    BlockchainData findByName(@NotNull String name, Pageable pageable);
    List<BlockchainData> findAll();
}
