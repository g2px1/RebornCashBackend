package com.client.authorizationService.repositories;

import com.client.authorizationService.models.DTO.users.User;
import com.client.authorizationService.services.openfeign.users.UserInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Service
@Repository
public class MongoRegisteredClientRepository implements RegisteredClientRepository {
    @Autowired
    private UserInterface userInterface;

    @Override
    public void save(RegisteredClient registeredClient) {
        userInterface.saveUser((User) registeredClient);
    }

    @Override
    public RegisteredClient findById(String id) {
        return null;
    }

    @Override
    public RegisteredClient findByClientId(String clientId) {
        return null;
    }
}
