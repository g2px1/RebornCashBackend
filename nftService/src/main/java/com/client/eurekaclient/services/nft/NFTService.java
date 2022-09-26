package com.client.eurekaclient.services.nft;

import com.client.eurekaclient.models.nft.NFT;
import com.client.eurekaclient.repositories.NFTRepository;
import com.client.eurekaclient.models.response.ResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.util.Map;
import java.util.Optional;

@Service
public class NFTService {
    @Autowired
    private NFTRepository NFTRepository;

    public Optional<NFT> findByIndex(long index) { return NFTRepository.findByIndex(index); }
    public ResponseEntity<Object> existsByIndex(long index) {return ResponseHandler.generateResponse(null, HttpStatus.OK, NFTRepository.existsByIndex(index));}
    public ResponseEntity<Object> findByName(String name) {return ResponseHandler.generateResponse(null, HttpStatus.OK, NFTRepository.findByName(name));}
    public ResponseEntity<Object> findByUuid(String uuid) {return ResponseHandler.generateResponse(null, HttpStatus.OK, NFTRepository.findByUuid(uuid));}
    public ResponseEntity<Object> save(NFT nft) {
        NFTRepository.save(nft);
        return ResponseHandler.generateResponse("success", HttpStatus.NO_CONTENT, null);
    }
    public ResponseEntity<Object> findAll(int pageNumber, String chain) {
        Pageable paging = PageRequest.of(pageNumber, 10);
        Page<NFT> page = NFTRepository.findAllByChain(chain, paging);
        return ResponseHandler.generateResponse(null, HttpStatus.OK, Map.of("content", page.getContent(), "currentPage", page.getNumber(), "totalItems", page.getTotalElements(), "totalPages", page.getTotalPages()));
    }
}
