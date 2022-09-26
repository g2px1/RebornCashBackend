package com.client.eurekaclient.services.lock;

import com.client.eurekaclient.models.lock.UserLock;
import com.client.eurekaclient.repositories.LockRepository;
import com.mongodb.DuplicateKeyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FairLock {
    @Autowired
    private LockRepository lockRepository;
    private static final Logger logger = LoggerFactory.getLogger(FairLock.class);

    public Optional<UserLock> getFairLock(String username) {
        UserLock userLock = new UserLock(username);
        try {
            lockRepository.save(userLock);
        } catch (DuplicateKeyException e) {
            logger.error(e.getMessage());
            return Optional.empty();
        }
        return Optional.of(userLock);
    }

    public boolean unlock(String username) {
        return lockRepository.deleteByUsername(username);
    }
}
