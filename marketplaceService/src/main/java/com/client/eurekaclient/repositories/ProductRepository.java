package com.client.eurekaclient.repositories;

import com.client.eurekaclient.models.market.AbstractProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ProductRepository extends MongoRepository<AbstractProduct, String> {
    Optional<AbstractProduct> findByUuid(String uuid);
    Optional<AbstractProduct> findByUuidAndStatus(String uuid, boolean status);
    boolean existsByUuid(String uuid);
    Page<AbstractProduct> findByStatus(boolean status, Pageable pageable);
    Page<AbstractProduct> findByTypeAndStatus(String type, boolean status, Pageable pageable);
}
