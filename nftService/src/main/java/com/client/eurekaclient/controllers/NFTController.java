package com.client.eurekaclient.controllers;

import com.client.eurekaclient.models.nft.NFT;
import com.client.eurekaclient.models.request.NFTPageRequest;
import com.client.eurekaclient.models.request.NFTSeekingRequest;
import com.client.eurekaclient.services.nft.NFTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/nftService")
public class NFTController {
    @Autowired
    private NFTService nftService;

    @PostMapping("/page")
    public ResponseEntity<Object> getNFTPage(@RequestBody NFTPageRequest nftPageRequest) {
        return nftService.findAllByPages(nftPageRequest.pageNumber, nftPageRequest.chain);
    }
    @PostMapping("/findNftByUuid")
    public ResponseEntity<Object> findNftByUuid(@RequestBody NFTSeekingRequest nftSeekingRequest) {
        return nftService.findByUuid(nftSeekingRequest.uuid);
    }
    @PostMapping("/findNftByName")
    public Optional<NFT> findNftByName(@RequestBody NFTSeekingRequest nftSeekingRequest) {
        return nftService.findByName(nftSeekingRequest.nftName);
    }
    @PostMapping("/nftService/findByIndex")
    public Optional<NFT> findByIndexAndChainName(@RequestBody NFTSeekingRequest nftSeekingRequest) {
        return nftService.findByIndexAndChainName(nftSeekingRequest.index, nftSeekingRequest.chainName);
    }
    @PostMapping("/existsByIndex")
    public ResponseEntity<Object> existsByIndex(@RequestBody NFTSeekingRequest nftSeekingRequest) {
        return nftService.existsByIndex(nftSeekingRequest.index);
    }
    @PostMapping("/findByIndex")
    public Optional<NFT> findByIndex(@RequestBody NFTSeekingRequest nftSeekingRequest) {
        return nftService.findByIndex(nftSeekingRequest.index);
    }
    @PostMapping("/findAll")
    public List<NFT> findAll() {
        return nftService.findAll();
    }
    @PostMapping("/save")
    public ResponseEntity<Object> findByIndex(@RequestBody NFT nft) {
        return nftService.save(nft);
    }
    @PostMapping("/save")
    public ResponseEntity<Object> saveAll(@RequestBody List<NFT> nft) {
        return nftService.saveAll(nft);
    }
}
