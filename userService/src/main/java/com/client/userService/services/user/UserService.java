package com.client.userService.services.user;

import com.client.userService.errors.messages.Errors;
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
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private RolesRepository rolesRepository;
    @Autowired
    private Errors errors;

    public Optional<User> getUser(String name) {
        return usersRepository.findFirstByUsername(name);
    }
    public Boolean createUser(User requestUser) {
        requestUser.setRoles(List.of(rolesRepository.findByName(ERole.ROLE_USER)));
        usersRepository.save(requestUser);
        return true;
    }
    public Boolean saveUser(User requestUser) {
        usersRepository.save(requestUser);
        return true;
    }
    public Boolean saveAll(List<User> requestUser) {
        usersRepository.saveAll(requestUser);
        return true;
    }
    public Boolean registerUser(User requestUser) {
        requestUser.setRoles(List.of(rolesRepository.findByName(ERole.ROLE_USER)));
        if(usersRepository.existsByUsername(requestUser.getUsername()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errors.USER_EXISTS);
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
    public Boolean updateUser(UserResponse userResponse) {
        Optional<User> user = usersRepository.findById(userResponse.id);
        if (user.isEmpty()) return false;
        user.get().updateUser(userResponse);
        usersRepository.save(user.get());
        return true;
    }

    public ResponseEntity<Object> loadUsersByPagination(UserSeekingRequest userSeekingRequest) {
        if (userSeekingRequest.status == null) {
            Pageable paging = PageRequest.of(userSeekingRequest.page, 10);
            Page<User> page = usersRepository.findAll(paging);
            return ResponseHandler.generateResponse(null, HttpStatus.OK, Map.of("content", page.getContent().stream().map(UserResponse::build).collect(Collectors.toList()), "currentPage", page.getNumber(), "totalItems", page.getTotalElements(), "totalPages", page.getTotalPages()));
        }
        Pageable paging = PageRequest.of(userSeekingRequest.page, 10);
        Page<User> page = usersRepository.findAllByStatus(userSeekingRequest.status, paging);
        return ResponseHandler.generateResponse(null, HttpStatus.OK, Map.of("content", page.getContent().stream().map(UserResponse::build).collect(Collectors.toList()), "currentPage", page.getNumber(), "totalItems", page.getTotalElements(), "totalPages", page.getTotalPages()));
    }

    public ResponseEntity<Object> getCode(String username) {
        User user = usersRepository.findFirstByUsername(username).get();
        if (user.getStatus().equalsIgnoreCase("banned")) return ResponseHandler.generateResponse(errors.USER_BANNED, HttpStatus.BAD_REQUEST, null);
        return ResponseHandler.generateResponse("", HttpStatus.OK, new ConcurrentHashMap<>(Map.of("qrLink", TimeBasedOneTimePasswordUtil.qrImageUrl(String.format("Reborn.Cash(username: %s)", user.getUsername()), user.getSecretKey()))));
    }

    public ResponseEntity<Object> set2FA(String username) {
        User user = usersRepository.findFirstByUsername(username).get();
        if (user.isTwoFA()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errors.DEFAULT_ERROR);
        }
        user.setTwoFA(true);
        user.setSecretKey(TimeBasedOneTimePasswordUtil.generateBase32Secret());
        usersRepository.save(user);
        return ResponseHandler.generateResponse("", HttpStatus.OK, new ConcurrentHashMap<>(Map.of("qrLink", TimeBasedOneTimePasswordUtil.qrImageUrl(String.format("Reborn.Cash(username: %s)", user.getUsername()), user.getSecretKey()), "secretKey", user.getSecretKey())));
    }
}
