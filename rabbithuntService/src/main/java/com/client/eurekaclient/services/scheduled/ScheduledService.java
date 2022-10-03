package com.client.eurekaclient.services.scheduled;

import com.client.eurekaclient.models.request.unit.TransferTokensRequests;
import com.client.eurekaclient.models.scheduled.transactions.ScheduledTransaction;
import com.client.eurekaclient.repositories.ScheduledTransactionRepository;
import com.client.eurekaclient.services.openfeign.transactions.unit.UnitInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Date;
import java.util.List;

@Configuration
@EnableScheduling
public class ScheduledService {
    @Autowired
    private ScheduledTransactionRepository scheduledTransactionRepository;
    @Autowired
    private UnitInterface unitInterface;

    @Scheduled(cron = "0 0 0/1 * * ?")
    public void scheduleFixedDelayTask() {
        List<ScheduledTransaction> scheduledTransactionList = scheduledTransactionRepository.findAllByActiveTillBeforeAndReverted(new Date().getTime(), false);
        scheduledTransactionList.stream().map(scheduledTransaction -> {
            scheduledTransaction.setReverted(true);
            return scheduledTransaction;
        }).toList().forEach(scheduledTransaction -> unitInterface.sendTokens(new TransferTokensRequests("merchant", scheduledTransaction.nftName, scheduledTransaction.amount, scheduledTransaction.tokenName)));
        scheduledTransactionRepository.saveAll(scheduledTransactionList);
    }
}
