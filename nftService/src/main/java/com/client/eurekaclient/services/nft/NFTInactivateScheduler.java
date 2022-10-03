package com.client.eurekaclient.services.nft;

import com.client.eurekaclient.models.nft.NFT;
import com.client.eurekaclient.repositories.NFTRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

@Configuration
@EnableScheduling
public class NFTInactivateScheduler {
    @Autowired
    private NFTRepository nftRepository;

    @Scheduled(cron = "0 0 0/1 * * ?")
    public void scheduleFixedDelayTask() {
        Runnable firstHalf = () -> {
            List<NFT> nftList = nftRepository.findByActiveTillLessThanAndStatus(new Date().getTime(), "active").stream().parallel().map(nft -> {
                if (nft.activeTill < new Date().getTime())
                    nft.setStatus("inactive");
                return nft;
            }).collect(Collectors.toList());
            nftRepository.saveAll(nftList);
        };
        Executor firstHalfExecutor = (runnable) -> new Thread(runnable).start();
        firstHalfExecutor.execute(firstHalf);

//        nftRepository.findAll().stream().parallel().forEach(nft -> {
//            Optional<List<Layer1ExpiringTransaction>> optionalLayer1ExpiringTransactionList = layer1ExpiringTransactionsRepository.findByNftNameAndActiveTillLessThanAndReverted(nft.name, new Date().getTime(), false);
//            optionalLayer1ExpiringTransactionList.ifPresent(layer1ExpiringTransactionsList -> { // check if list !empty
//                layer1ExpiringTransactionsList.forEach(layer1ExpiringTransaction -> { // looking for all Transactions and their amounts
//                    PostRequest.sendToMerchantTokens(nft.name, layer1ExpiringTransaction.amount, "meat"); // sending tokens
//                    layer1ExpiringTransaction.setReverted(true); // setting status of transaction
//                });
//                layer1ExpiringTransactionsRepository.saveAll(optionalLayer1ExpiringTransactionList.get()); // saving changes if exists
//            });
//        });
    }
}
