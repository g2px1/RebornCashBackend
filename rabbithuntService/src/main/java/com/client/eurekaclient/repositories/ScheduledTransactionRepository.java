package com.client.eurekaclient.repositories;

import com.client.eurekaclient.models.scheduled.transactions.ScheduledTransaction;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ScheduledTransactionRepository extends MongoRepository<ScheduledTransaction, String> {
    Optional<List<ScheduledTransaction>> findByNftNameAndActiveTillLessThanAndReverted(String name, long activeTill, boolean reverted);
    ScheduledTransaction findFirstByNftName(String name);
    List<ScheduledTransaction> findAllByActiveTillBeforeAndReverted(long date, boolean reverted);
}
