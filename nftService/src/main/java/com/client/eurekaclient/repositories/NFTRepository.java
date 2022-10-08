package com.client.eurekaclient.repositories;

import com.client.eurekaclient.models.nft.NFT;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface NFTRepository extends MongoRepository<NFT, String> {
    Optional<NFT> findByIndex(long index);
    Optional<NFT> findByIndexAndChain(long index, String chainName);
    Boolean existsByIndex(long index);
    List<NFT> findByIndexBetween(long index, long index2);
    Optional<NFT> findByName(String name);
    Optional<NFT> findByUuid(String uuid);
    Page<NFT> findAllByChain(String chain, Pageable pageable);
    List<NFT> findByActiveTillLessThanAndStatus(long date, String status);
    List<NFT> findAll();
}
