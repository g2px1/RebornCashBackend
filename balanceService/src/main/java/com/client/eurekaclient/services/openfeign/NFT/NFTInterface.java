package com.client.eurekaclient.services.openfeign.NFT;

import com.client.eurekaclient.models.nft.NFT;
import com.client.eurekaclient.models.request.web3.NFTSeekingRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

@FeignClient(name = "nftService")
public interface NFTInterface {
    @PostMapping("/nftService/findByIndex")
    Optional<NFT> findByIndex(@RequestBody NFTSeekingRequest nftSeekingRequest);
    @PostMapping("/nftService/findByIndex")
    Optional<NFT> saveAll(@RequestBody List<NFT> nftList);
    @PostMapping("/nftService/findByIndex")
    Optional<NFT> findByIndexAndChainName(@RequestBody NFTSeekingRequest nftSeekingRequest);
    @PostMapping("/findNftByName")
    Optional<NFT> findNftByName(@RequestBody NFTSeekingRequest nftSeekingRequest);
    @PostMapping("/findNftByName")
    Optional<NFT> findNftByUuid(@RequestBody NFTSeekingRequest nftSeekingRequest);
}
