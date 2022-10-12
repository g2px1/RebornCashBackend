package com.client.eurekaclient.services.lock;

import com.client.eurekaclient.models.lock.CellsLock;
import com.client.eurekaclient.models.lock.UserLock;
import com.client.eurekaclient.repositories.CellsLockRepository;
import com.client.eurekaclient.repositories.UserLockRepository;
import com.mongodb.DuplicateKeyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FairLock {
    @Autowired
    private UserLockRepository userLockRepository;
    @Autowired
    private CellsLockRepository cellsLockRepository;
    private static final Logger logger = LoggerFactory.getLogger(FairLock.class);

    public Optional<UserLock> getUserFairLock(String username) {
        UserLock userLock = new UserLock(username);
        try {
            userLockRepository.save(userLock);
        } catch (DuplicateKeyException e) {
            logger.error(e.getMessage());
            return Optional.empty();
        }
        return Optional.of(userLock);
    }

    public Optional<CellsLock> getCellsLock(String uuid) {
        CellsLock cellsLock = new CellsLock(uuid);
        try {
            cellsLockRepository.save(cellsLock);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return Optional.empty();
        }
        return Optional.of(cellsLock);
    }

    public long unlockUserLock(String username) {
        return userLockRepository.deleteByUsername(username);
    }
    public boolean unlockCellsLock(String uuid) {
        return cellsLockRepository.deleteByCellsPackUuid(uuid);
    }
}
