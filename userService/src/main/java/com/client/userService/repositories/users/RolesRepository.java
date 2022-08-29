package com.client.userService.repositories.users;

import com.client.userService.models.dbModels.users.ERole;
import com.client.userService.models.dbModels.users.Role;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;
import java.util.Optional;

public interface RolesRepository extends ReactiveMongoRepository<Role, String> {
    Mono<Optional<Role>> findByName(ERole name);
}
