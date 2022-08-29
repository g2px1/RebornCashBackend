package com.client.userService.repositories.users;

import com.client.userService.models.dbModels.users.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends MongoRepository<User, String> {
    Optional<User> findFirstByUsername(String username);
    Boolean existsByUsername(String username);
}