package com.client.userService.repositories.users;

import com.client.userService.models.dbModels.users.ERole;
import com.client.userService.models.dbModels.users.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RolesRepository extends MongoRepository<Role, String> {
    Role findByName(ERole name);
}
