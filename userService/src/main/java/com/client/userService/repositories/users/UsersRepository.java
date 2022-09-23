package com.client.userService.repositories.users;

import com.client.userService.models.DTO.users.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends MongoRepository<User, String> {
    Optional<User> findFirstByUsername(String username);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
    Boolean existsByUsernameOrEmail(String username, String email);
    Page<User> findAllByStatus(String type, Pageable pageable);
    Page<User> findAll(Pageable pageable);
}