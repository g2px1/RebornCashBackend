package com.client.eurekaclient.repositories;

import com.client.eurekaclient.models.rabbithunt.cells.CellsPack;
import com.client.eurekaclient.models.rabbithunt.trap.Trap;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface CellsPackRepository extends MongoRepository<CellsPack, String> {
    Optional<CellsPack> findByTrapAndSellerNFTName(Trap trap, String sellerName);
    Optional<CellsPack> findByIndex(long index);
    Optional<CellsPack> findFirstByIndexAndSellerNFTNameAndStatus(long index, String sellerName, String status);
    List<CellsPack> findByTrap(Trap trap);
}
