package com.client.eurekaclient.repositories;

import com.client.eurekaclient.models.goldenrush.mine.CellsTransactions;
import com.client.eurekaclient.models.goldenrush.mine.Mine;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface CellsTransactionRepository extends MongoRepository<CellsTransactions, String> {
    Optional<List<CellsTransactions>> findByTrapAndNftName(Mine mine, String nftName);
    Optional<List<CellsTransactions>> findByMine(Mine mine);
}
