package com.client.eurekaclient.services.openfeign.NFT;

import com.client.eurekaclient.models.nft.NFT;
import com.client.eurekaclient.models.request.NFT.NFTSeekingRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "nftService")
public interface NFTInterface {
    @PostMapping("/nftService/findByIndex")
    NFT findByIndex(@RequestBody NFTSeekingRequest nftSeekingRequest);
}
