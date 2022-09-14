package com.client.eurekaclient.services;

import com.client.eurekaclient.models.response.ResponseHandler;
import com.client.eurekaclient.models.web3.Transaction;
import com.client.eurekaclient.repositories.TransactionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class TransactionService {
    @Autowired
    private TransactionsRepository transactionsRepository;

    public ResponseEntity<Object> getUserTransaction(String username, String chain, int pageNumber) {
        Pageable paging = PageRequest.of(pageNumber, 10);
        Page<Transaction> page = transactionsRepository.findByUsernameAndChainName(username, chain, paging);
        return ResponseHandler.generateResponse(null, HttpStatus.OK, Map.of("content", page.getContent(), "currentPage", page.getNumber(), "totalItems", page.getTotalElements(), "totalPages", page.getTotalPages()));
    }
}
