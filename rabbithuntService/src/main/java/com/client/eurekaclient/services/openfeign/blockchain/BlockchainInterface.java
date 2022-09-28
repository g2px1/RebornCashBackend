package com.client.eurekaclient.services.openfeign.blockchain;

import com.client.eurekaclient.models.DTO.blockchain.data.BlockchainDataResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@FeignClient(name = "balanceService")
public interface BlockchainInterface {
    @PostMapping("/getBlockchainData/{chainName}")
    Optional<BlockchainDataResponse> getBlockchainData(@PathVariable String chainName);
}
