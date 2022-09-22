package com.client.eurekaclient.services.web3;

import com.client.eurekaclient.messages.ErrorMessage;
import com.client.eurekaclient.models.DTO.users.User;
import com.client.eurekaclient.models.response.ResponseHandler;
import com.client.eurekaclient.models.web3.BlockchainData;
import com.client.eurekaclient.models.web3.Transaction;
import com.client.eurekaclient.repositories.BlockchainsRepository;
import com.client.eurekaclient.repositories.TransactionsRepository;
import com.client.eurekaclient.services.openfeign.users.UserInterface;
import com.client.eurekaclient.services.web3.contracts.StandardContractProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;
import java.util.Optional;

@Service
public class TransactionService {
    @Autowired
    private TransactionsRepository transactionsRepository;
    @Autowired
    private BlockchainsRepository blockchainsRepository;
    @Autowired
    private UserInterface userInterface;

    public ResponseEntity<Object> getUserTransaction(String username, String chain, int pageNumber) {
        Pageable paging = PageRequest.of(pageNumber, 10);
        Page<Transaction> page = transactionsRepository.findByUsernameAndChainName(username, chain, paging);
        return ResponseHandler.generateResponse(null, HttpStatus.OK, Map.of("content", page.getContent(), "currentPage", page.getNumber(), "totalItems", page.getTotalElements(), "totalPages", page.getTotalPages()));
    }

    public ResponseEntity<Object> saveTransaction(String username, String chainName, String hash) {
        if (transactionsRepository.existsByHashAndChainName(hash, chainName)) return ResponseHandler.generateResponse("transaction already exists.", HttpStatus.BAD_REQUEST, false);
        Optional<BlockchainData> optionalBlockchainData = blockchainsRepository.findByName(chainName);
        if (optionalBlockchainData.isEmpty()) return ResponseHandler.generateResponse(ErrorMessage.CHAIN_NOT_SUPPORTED, HttpStatus.BAD_REQUEST, false);
        BlockchainData blockchainData = optionalBlockchainData.get();
        StandardContractProvider standardContractProvider = new StandardContractProvider(blockchainData.url, blockchainData.privateKey);
        Optional<org.web3j.protocol.core.methods.response.Transaction>transaction;
        try {
            transaction = standardContractProvider.getWeb3j().ethGetTransactionByHash(hash).send().getTransaction();
            if (transaction.isEmpty()) return ResponseHandler.generateResponse("bad transaction: not found.", HttpStatus.BAD_REQUEST, false);
            if (!transaction.get().getTo().equalsIgnoreCase(blockchainData.hotWalletAddress)) return ResponseHandler.generateResponse("bad transaction: not our address.", HttpStatus.BAD_REQUEST, false);
        } catch (IOException e) {
            return ResponseHandler.generateResponse("error occurred: cannot send request to WEB3 network", HttpStatus.BAD_REQUEST, false);
        }
        Optional<User> optionalUser = userInterface.getUser(username);
        if (optionalUser.isEmpty()) return ResponseHandler.generateResponse(ErrorMessage.USER_NOT_FOUND, HttpStatus.BAD_REQUEST, false);
        User user = optionalUser.get();
        BigDecimal amount = new BigDecimal(new BigInteger(transaction.get().getInput().substring(74), 16)).divide(BigDecimal.valueOf(Math.pow(10, 18)));
        user.setBalance(user.getBalance().add(amount));
        transactionsRepository.save(new Transaction(hash, chainName, username));
        userInterface.saveUser(user);
        return ResponseHandler.generateResponse(null, HttpStatus.BAD_REQUEST, true);
    }
}
