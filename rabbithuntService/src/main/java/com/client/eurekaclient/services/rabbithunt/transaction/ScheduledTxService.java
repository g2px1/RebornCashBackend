package com.client.eurekaclient.services.rabbithunt.transaction;

import com.client.eurekaclient.models.scheduled.transactions.ScheduledTransaction;
import com.client.eurekaclient.repositories.ScheduledTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScheduledTxService {
    @Autowired
    private ScheduledTransactionRepository scheduledTransactionRepository;

    public void subtractTxByNft(String nftName, double amount) {
        ScheduledTransaction scheduledTransaction = scheduledTransactionRepository.findFirstByNftName(nftName);
        boolean isEnoughToSubtract = true;
        while (isEnoughToSubtract) {
            if (scheduledTransaction.amount < amount) {
                scheduledTransactionRepository.delete(scheduledTransaction);
                amount -= scheduledTransaction.amount;
                scheduledTransactionRepository.save(scheduledTransaction);
            } else {
                scheduledTransaction.setAmount(scheduledTransaction.amount - amount);
                isEnoughToSubtract = false;
            }
            scheduledTransaction = scheduledTransactionRepository.findFirstByNftName(nftName);
            if (scheduledTransaction == null)
                isEnoughToSubtract = false;
        }
    }
}
