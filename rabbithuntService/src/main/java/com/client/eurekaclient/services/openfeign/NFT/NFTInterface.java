package com.client.eurekaclient.services.openfeign.NFT;

import com.client.eurekaclient.models.nft.NFT;
import com.client.eurekaclient.models.request.NFT.NFTSeekingRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@FeignClient(name = "nftService")
public interface NFTInterface {
    @PostMapping("/nftService/findByIndex")
    Optional<NFT> findByIndex(@RequestBody NFTSeekingRequest nftSeekingRequest);
}
