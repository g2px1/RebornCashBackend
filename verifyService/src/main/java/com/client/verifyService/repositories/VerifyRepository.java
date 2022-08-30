package com.client.verifyService.repositories;

import com.client.verifyService.models.Verify2FA;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface VerifyRepository extends MongoRepository<Verify2FA, String> {
    Verify2FA findByUsername(String username);
    boolean existsByUsername(String username);
    void deleteByUsername(String username);
    void deleteAllByUsername(String username);
}
