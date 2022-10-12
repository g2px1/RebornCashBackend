package com.client.eurekaclient.repositories;

import com.client.eurekaclient.models.lock.UserLock;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserLockRepository extends MongoRepository<UserLock, String> {
    Optional<UserLock> findByUsername(String username);
    long deleteByUsername(String username);
}
