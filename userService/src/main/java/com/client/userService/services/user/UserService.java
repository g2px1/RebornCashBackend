package com.client.userService.services.user;

import com.client.userService.errors.messages.ErrorMessage;
import com.client.userService.models.dbModels.users.User;
import com.client.userService.repositories.users.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UsersRepository usersRepository;

    public Optional<User> getUser(String name) {
        return usersRepository.findFirstByUsername(name);
    }
    public Boolean saveUser(User requestUser) {
        if(!usersRepository.existsByUsername(requestUser.getUsername()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessage.USER_EXISTS);
        usersRepository.save(requestUser);
        return true;
    }
    public Boolean userExists(String username) {
        return usersRepository.existsByUsername(username);
    }
}
