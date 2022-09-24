package com.client.eurekaclient.repositories;

import com.client.eurekaclient.models.rabbithunt.trap.Trap;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TrapRepository extends MongoRepository<Trap, String> {
    boolean existsByName(String name);
    Trap findByName(String name);
    Page<Trap> findAllByStatus(@NotNull String status, Pageable pageable);
}
