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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;
import java.util.function.Function;

@Service
public class Web3Service {
    @Autowired
    private BlockchainsRepository blockchainsRepository;
    @Autowired
    private TransactionsRepository transactionsRepository;
    @Autowired
    private UserInterface userInterface;
    private static final Function<Double, Double> rounder = j -> Math.round(j * 10000.0) / 10000.0;

    public ResponseEntity<Object> sendStableCoinTransaction(String recipientAddress, String chainName, double amount, String username) {
        Optional<BlockchainData> blockchainDataOptional = blockchainsRepository.findByName(chainName);
        if (blockchainDataOptional.isEmpty()) return ResponseHandler.generateResponse(ErrorMessage.CHAIN_NOT_SUPPORTED, HttpStatus.OK, null);
        BlockchainData blockchainData = blockchainDataOptional.get();
        StandardContractProvider standardContractProvider = new StandardContractProvider(blockchainData.url, blockchainData.getPrivateKey());
        amount = Web3Service.rounder.apply(amount);
        BigInteger bigIntegerAmount = BigInteger.valueOf((long) (amount * 10000)).multiply(BigInteger.valueOf(10).pow(14));
        try {
            TransactionReceipt transactionReceipt = standardContractProvider.getERC20Token(blockchainData.stableCoinAddress).transfer(recipientAddress, bigIntegerAmount).send();
            transactionsRepository.save(new Transaction(transactionReceipt.getTransactionHash(), chainName, username));
            return ResponseHandler.generateResponse(null, HttpStatus.OK, transactionReceipt.toString());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseHandler.generateResponse(String.format(ErrorMessage.TRANSACTION_ERROR, e.getMessage()) , HttpStatus.BAD_REQUEST, null);
        }
    }
    public ResponseEntity<Object> sendGameTransaction(String recipientAddress, String chainName, double amount, String username) {
        Optional<BlockchainData> blockchainDataOptional = blockchainsRepository.findByName(chainName);
        if (blockchainDataOptional.isEmpty()) return ResponseHandler.generateResponse(ErrorMessage.CHAIN_NOT_SUPPORTED, HttpStatus.OK, null);
        BlockchainData blockchainData = blockchainDataOptional.get();
        StandardContractProvider standardContractProvider = new StandardContractProvider(blockchainData.url, blockchainData.getPrivateKey());
        amount = Web3Service.rounder.apply(amount);
        BigInteger bigIntegerAmount = BigInteger.valueOf((long) (amount * 10000)).multiply(BigInteger.valueOf(10).pow(14));
        Optional<User> optionalUser = userInterface.getUser(username);
        if (optionalUser.isEmpty()) return ResponseHandler.generateResponse(ErrorMessage.USER_NOT_FOUND, HttpStatus.BAD_REQUEST, false);
        User user = optionalUser.get();
        if (user.getBalance().compareTo(new BigDecimal(bigIntegerAmount)) < 0) return ResponseHandler.generateResponse(ErrorMessage.LOW_GAME_BALANCE, HttpStatus.BAD_REQUEST, false);
        try {
            TransactionReceipt transactionReceipt = standardContractProvider.getERC20Token(blockchainData.gameContractAddress).transfer(recipientAddress, bigIntegerAmount).send();
            transactionsRepository.save(new Transaction(transactionReceipt.getTransactionHash(), chainName, username));
            return ResponseHandler.generateResponse(null, HttpStatus.OK, transactionReceipt.toString());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseHandler.generateResponse(String.format(ErrorMessage.TRANSACTION_ERROR, e.getMessage()) , HttpStatus.BAD_REQUEST, null);
        }
    }
}
