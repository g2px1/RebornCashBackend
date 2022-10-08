package com.client.eurekaclient.repositories;

import com.client.eurekaclient.models.goldenrush.mine.Mine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MineRepository extends MongoRepository<Mine, String> {
    boolean existsByName(String name);
    Mine findByName(String name);
    Page<Mine> findAllByStatus(boolean status, Pageable pageable);
}
