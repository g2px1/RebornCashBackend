package com.client.eurekaclient.repositories;

import com.client.eurekaclient.models.NFT;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

public interface ReactiveNFTRepository extends ReactiveMongoRepository<NFT, String> {
    Mono<NFT> findByIndex(long index);
    Mono<Boolean> existsByIndex(long index);
    Flux<NFT> findByIndexBetween(long index, long index2);
    Optional<NFT> findByName(String name);
}
