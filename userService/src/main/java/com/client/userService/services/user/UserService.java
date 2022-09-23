package com.client.userService.services.user;

import com.client.userService.errors.messages.ErrorMessage;
import com.client.userService.models.request.UserSeekingRequest;
import com.client.userService.models.response.ResponseHandler;
import com.client.userService.models.DTO.users.ERole;
import com.client.userService.models.DTO.users.User;
import com.client.userService.models.response.UserResponse;
import com.client.userService.repositories.users.RolesRepository;
import com.client.userService.repositories.users.UsersRepository;
import com.j256.twofactorauth.TimeBasedOneTimePasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private RolesRepository rolesRepository;

    public Optional<User> getUser(String name) {
        return usersRepository.findFirstByUsername(name);
    }
    public Boolean saveUser(User requestUser) {
        requestUser.setRoles(List.of(rolesRepository.findByName(ERole.ROLE_USER)));
        usersRepository.save(requestUser);
        return true;
    }
    public Boolean registerUser(User requestUser) {
        requestUser.setRoles(List.of(rolesRepository.findByName(ERole.ROLE_USER)));
        if(usersRepository.existsByUsername(requestUser.getUsername()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessage.USER_EXISTS);
        usersRepository.save(requestUser);
        return true;
    }
    public Boolean userExists(String username) {
        return usersRepository.existsByUsername(username);
    }
    public Boolean userExistsByEmail(String email) { return usersRepository.existsByEmail(email); }
    public Boolean userExistsByAll(String username, String email) { return usersRepository.existsByUsernameOrEmail(username, email); }
    public Boolean changePasswordIfExists(String username, String newPassword, String code) throws GeneralSecurityException {
        Optional<User> user = usersRepository.findFirstByUsername(username);
        if (user.isEmpty()) return false;
        if (TimeBasedOneTimePasswordUtil.validateCurrentNumber(user.get().getSecretKey(), Integer.parseInt(code), 0)) return false;
        user.get().setPassword(newPassword);
        usersRepository.save(user.get());
        return true;
    }
    public Boolean updateUser(User user) {
        if (!usersRepository.existsByUsername(user.getUsername())) return false;
        usersRepository.save(user);
        return true;
    }

    public ResponseEntity<Object> loadUsersByPagination(UserSeekingRequest userSeekingRequest) {
        if (userSeekingRequest.status == null) {
            Pageable paging = PageRequest.of(userSeekingRequest.page, 5);
            Page<User> page = usersRepository.findAll(paging);
            return ResponseHandler.generateResponse(null, HttpStatus.OK, Map.of("content", page.getContent().stream().map(UserResponse::build).collect(Collectors.toList()), "currentPage", page.getNumber(), "totalItems", page.getTotalElements(), "totalPages", page.getTotalPages()));
        }
        Pageable paging = PageRequest.of(userSeekingRequest.page, 5);
        Page<User> page = usersRepository.findAllByStatus(userSeekingRequest.status, paging);
        return ResponseHandler.generateResponse(null, HttpStatus.OK, Map.of("content", page.getContent().stream().map(UserResponse::build).collect(Collectors.toList()), "currentPage", page.getNumber(), "totalItems", page.getTotalElements(), "totalPages", page.getTotalPages()));
    }
}
