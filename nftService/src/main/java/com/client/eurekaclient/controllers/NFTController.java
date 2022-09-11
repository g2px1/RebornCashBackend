package com.client.eurekaclient.controllers;

import com.client.eurekaclient.models.request.NFTPageRequest;
import com.client.eurekaclient.models.request.NFTSeekingRequest;
import com.client.eurekaclient.services.nft.NFTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/nftService")
public class NFTController {
    @Autowired
    private NFTService nftService;

    @PostMapping("/page")
    public ResponseEntity<Object> getNFTPage(@RequestBody NFTPageRequest nftPageRequest) {
        return nftService.findAll(nftPageRequest.pageNumber, nftPageRequest.chain);
    }
    @PostMapping("/findNftByUuid")
    public ResponseEntity<Object> findNftByUuid(@RequestBody NFTSeekingRequest nftSeekingRequest) {
        return nftService.findByUuid(nftSeekingRequest.uuid);
    }
    @PostMapping("/findNftByName")
    public ResponseEntity<Object> findNftByName(@RequestBody NFTSeekingRequest nftSeekingRequest) {
        return nftService.findByName(nftSeekingRequest.nftName);
    }
    @PostMapping("/existsByIndex")
    public ResponseEntity<Object> existsByIndex(@RequestBody NFTSeekingRequest nftSeekingRequest) {
        return nftService.existsByIndex(nftSeekingRequest.index);
    }
    @PostMapping("/findByIndex")
    public ResponseEntity<Object> findByIndex(@RequestBody NFTSeekingRequest nftSeekingRequest) {
        return nftService.findByIndex(nftSeekingRequest.index);
    }
}
