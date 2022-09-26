package com.client.eurekaclient.repositories;

import com.client.eurekaclient.models.rabbithunt.trap.CellsTransactions;
import com.client.eurekaclient.models.rabbithunt.trap.Trap;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface CellsTransactionRepository extends MongoRepository<CellsTransactions, String> {
    Optional<List<CellsTransactions>> findByTrapAndNftName(Trap trap, String nftName);
}
