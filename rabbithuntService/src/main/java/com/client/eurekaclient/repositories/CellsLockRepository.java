package com.client.eurekaclient.repositories;

import com.client.eurekaclient.models.lock.CellsLock;
import com.client.eurekaclient.models.lock.UserLock;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CellsLockRepository extends MongoRepository<CellsLock, String> {
    Optional<CellsLock> findByCellsPackUuid(String uuid);
    boolean deleteByCellsPackUuid(String uuid);
}