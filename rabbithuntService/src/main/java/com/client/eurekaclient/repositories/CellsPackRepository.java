package com.client.eurekaclient.repositories;

import com.client.eurekaclient.models.goldenrush.cells.CellsPack;
import com.client.eurekaclient.models.goldenrush.mine.Mine;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface CellsPackRepository extends MongoRepository<CellsPack, String> {
    Optional<CellsPack> findByTrapAndNftName(Mine mine, String sellerName);
    Optional<CellsPack> findByIndex(long index);
    Optional<CellsPack> findFirstByIndexAndNftNameAndStatus(long index, String sellerName, String status);
    List<CellsPack> findByTrap(Mine mine);
}
