package com.client.eurekaclient.services.web3;

import com.client.eurekaclient.messages.ErrorMessage;
import com.client.eurekaclient.models.DTO.transactions.TransactionResult;
import com.client.eurekaclient.models.DTO.users.User;
import com.client.eurekaclient.models.lock.UserLock;
import com.client.eurekaclient.models.response.BlockchainDataResponse;
import com.client.eurekaclient.models.response.ResponseHandler;
import com.client.eurekaclient.models.web3.BlockchainData;
import com.client.eurekaclient.models.web3.ConnectedWallet;
import com.client.eurekaclient.models.web3.Transaction;
import com.client.eurekaclient.repositories.BlockchainsRepository;
import com.client.eurekaclient.repositories.ConnectedWalletsRepository;
import com.client.eurekaclient.repositories.LockRepository;
import com.client.eurekaclient.repositories.TransactionsRepository;
import com.client.eurekaclient.services.lock.FairLock;
import com.client.eurekaclient.services.openfeign.users.UserInterface;
import com.client.eurekaclient.services.web3.contracts.StandardContractProvider;
import com.j256.twofactorauth.TimeBasedOneTimePasswordUtil;
import com.jayway.jsonpath.JsonPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.util.Locale;
import java.util.Map;
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
    @Autowired
    private ConnectedWalletsRepository connectedWalletsRepository;
    @Autowired
    private FairLock fairLock;
    private static final Function<Double, Double> rounder = j -> Math.round(j * 10000.0) / 10000.0;
    private static final Logger logger = LoggerFactory.getLogger(Web3Service.class);

    public TransactionResult sendStableCoinTransaction(String recipientAddress, String chainName, double amount, String username) {
        Optional<BlockchainData> blockchainDataOptional = blockchainsRepository.findByName(chainName);
        if (blockchainDataOptional.isEmpty()) return new TransactionResult(null, true, ErrorMessage.CHAIN_NOT_SUPPORTED);
        BlockchainData blockchainData = blockchainDataOptional.get();
        StandardContractProvider standardContractProvider = new StandardContractProvider(blockchainData.url, blockchainData.getPrivateKey());
        amount = Web3Service.rounder.apply(amount);
        BigInteger bigIntegerAmount = BigInteger.valueOf((long) (amount * 10000)).multiply(BigInteger.valueOf(10).pow(14));
        try {
            TransactionReceipt transactionReceipt = standardContractProvider.getERC20Token(blockchainData.stableCoinAddress).transfer(recipientAddress, bigIntegerAmount).send();
            if (transactionsRepository.existsByHashAndChainName(transactionReceipt.getTransactionHash(), chainName)) return new TransactionResult(null, true, String.format(ErrorMessage.TRANSACTION_ERROR, "transaction exists"));
            transactionsRepository.save(new Transaction(transactionReceipt.getTransactionHash(), chainName, username));
            return new TransactionResult(transactionReceipt, false, null);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new TransactionResult(null, false, ErrorMessage.TRANSACTION_ERROR);
        }
    }

    public TransactionResult sendSecuredStableCoinTransaction(String chainName, double amount, String username) {
        Optional<BlockchainData> blockchainDataOptional = blockchainsRepository.findByName(chainName);
        if (blockchainDataOptional.isEmpty()) return new TransactionResult(null, true, ErrorMessage.CHAIN_NOT_SUPPORTED);
        BlockchainData blockchainData = blockchainDataOptional.get();
        StandardContractProvider standardContractProvider = new StandardContractProvider(blockchainData.url, blockchainData.getPrivateKey());
        amount = Web3Service.rounder.apply(amount);
        BigInteger bigIntegerAmount = BigInteger.valueOf((long) (amount * 10000)).multiply(BigInteger.valueOf(10).pow(14));
        try {
            Optional<ConnectedWallet> connectedWallet = connectedWalletsRepository.findByUsername(username);
            if (connectedWallet.isEmpty()) return new TransactionResult(null, true, ErrorMessage.METAMASK_ERROR);
            TransactionReceipt transactionReceipt = standardContractProvider.getERC20Token(blockchainData.stableCoinAddress).transfer(connectedWallet.get().getAddress(), bigIntegerAmount).send();
            if (transactionsRepository.existsByHashAndChainName(transactionReceipt.getTransactionHash(), chainName)) return new TransactionResult(null, true, String.format(ErrorMessage.TRANSACTION_ERROR, "transaction exists"));
            transactionsRepository.save(new Transaction(transactionReceipt.getTransactionHash(), chainName, username));
            return new TransactionResult(transactionReceipt, false, null);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new TransactionResult(null, false, ErrorMessage.TRANSACTION_ERROR);
        }
    }
    public ResponseEntity<Object> sendGameTransaction(String recipientAddress, String chainName, double amount, String username, String code) {
        Optional<UserLock> userLock = fairLock.getFairLock(username);
        if (userLock.isEmpty()) return ResponseHandler.generateResponse(ErrorMessage.LOCK, HttpStatus.OK, null);
        Optional<BlockchainData> blockchainDataOptional = blockchainsRepository.findByName(chainName);
        if (blockchainDataOptional.isEmpty()) {
            fairLock.unlock(username);
            return ResponseHandler.generateResponse(ErrorMessage.CHAIN_NOT_SUPPORTED, HttpStatus.OK, null);
        }
        BlockchainData blockchainData = blockchainDataOptional.get();
        StandardContractProvider standardContractProvider = new StandardContractProvider(blockchainData.url, blockchainData.getPrivateKey());
        amount = Web3Service.rounder.apply(amount);
        BigInteger bigIntegerAmount = BigInteger.valueOf((long) (amount * 10000)).multiply(BigInteger.valueOf(10).pow(14));
        Optional<User> optionalUser = userInterface.getUser(username);
        if (optionalUser.isEmpty()) {
            fairLock.unlock(username);
            return ResponseHandler.generateResponse(ErrorMessage.USER_NOT_FOUND, HttpStatus.BAD_REQUEST, false);
        }
        User user = optionalUser.get();
        if (user.getBalance().compareTo(new BigDecimal(bigIntegerAmount)) < 0) {
            fairLock.unlock(username);
            return ResponseHandler.generateResponse(ErrorMessage.LOW_GAME_BALANCE, HttpStatus.BAD_REQUEST, false);
        }
        try {
            if (!TimeBasedOneTimePasswordUtil.validateCurrentNumber(user.getSecretKey(), Integer.parseInt(code), 0)) {
                fairLock.unlock(username);
                return ResponseHandler.generateResponse(ErrorMessage.INVALID_CODE, HttpStatus.BAD_REQUEST, false);
            }
        } catch (GeneralSecurityException e) {
            logger.error(e.getMessage());
            fairLock.unlock(username);
            return ResponseHandler.generateResponse(ErrorMessage.DEFAULT_ERROR, HttpStatus.BAD_REQUEST, false);
        }
        try {
            TransactionReceipt transactionReceipt = standardContractProvider.getERC20Token(blockchainData.gameContractAddress).transfer(recipientAddress, bigIntegerAmount).send();
            if (transactionsRepository.existsByHashAndChainName(transactionReceipt.getTransactionHash(), chainName)){
                fairLock.unlock(username);
                return ResponseHandler.generateResponse(String.format(ErrorMessage.TRANSACTION_ERROR, "transaction exists"), HttpStatus.BAD_REQUEST, false);
            }
            transactionsRepository.save(new Transaction(transactionReceipt.getTransactionHash(), chainName, username));
            fairLock.unlock(username);
            return ResponseHandler.generateResponse(null, HttpStatus.OK, transactionReceipt.toString());
        } catch (Exception e) {
            logger.error(e.getMessage());
            fairLock.unlock(username);
            return ResponseHandler.generateResponse(String.format(ErrorMessage.TRANSACTION_ERROR, e.getMessage()) , HttpStatus.BAD_REQUEST, null);
        }
    }
    public TransactionResult sendSecuredGameTransaction(String chainName, double amount, String username) {
        Optional<BlockchainData> blockchainDataOptional = blockchainsRepository.findByName(chainName);
        if (blockchainDataOptional.isEmpty()) return new TransactionResult(null, true, ErrorMessage.CHAIN_NOT_SUPPORTED);
        BlockchainData blockchainData = blockchainDataOptional.get();
        StandardContractProvider standardContractProvider = new StandardContractProvider(blockchainData.url, blockchainData.getPrivateKey());
        amount = Web3Service.rounder.apply(amount);
        BigInteger bigIntegerAmount = BigInteger.valueOf((long) (amount * 10000)).multiply(BigInteger.valueOf(10).pow(14));
        try {
            Optional<ConnectedWallet> connectedWallet = connectedWalletsRepository.findByUsername(username);
            if (connectedWallet.isEmpty()) return new TransactionResult(null, true, ErrorMessage.METAMASK_ERROR);
            TransactionReceipt transactionReceipt = standardContractProvider.getERC20Token(blockchainData.gameContractAddress).transfer(blockchainData.gameIMMO, bigIntegerAmount).send();
            if (transactionsRepository.existsByHashAndChainName(transactionReceipt.getTransactionHash(), chainName)) return new TransactionResult(null, true, String.format(ErrorMessage.TRANSACTION_ERROR, "transaction exists"));
            transactionsRepository.save(new Transaction(transactionReceipt.getTransactionHash(), chainName, username));
            return new TransactionResult(transactionReceipt, false, null);
        } catch (Exception e) {
            logger.error(e.getMessage());
            fairLock.unlock(username);
            return new TransactionResult(null, false, ErrorMessage.TRANSACTION_ERROR);
        }
    }

    public ResponseEntity<Object> validateParsing(String pathToJson, String json) {
        try {
            String data = JsonPath.read(json, String.format("$.%s", pathToJson));
            return ResponseHandler.generateResponse(null , HttpStatus.BAD_REQUEST, Map.of("parsedData", data));
        } catch (Exception e) {
            return ResponseHandler.generateResponse(ErrorMessage.DEFAULT_ERROR , HttpStatus.BAD_REQUEST, true);
        }
    }

    public ResponseEntity<Object> addBlockchain(BlockchainData blockchainData) {
        if (blockchainsRepository.existsByUrlOrName(blockchainData.url, blockchainData.name.toLowerCase(Locale.ROOT))) return ResponseHandler.generateResponse(ErrorMessage.BLOCKCHAIN_ALREADY_ADDED , HttpStatus.BAD_REQUEST, null);
        blockchainsRepository.save(blockchainData);
        return ResponseHandler.generateResponse(null , HttpStatus.BAD_REQUEST, true);
    }

    public Optional<BlockchainDataResponse> getBlockchain(String chainName) {
        Optional<BlockchainData> optionalBlockchainData = blockchainsRepository.findByName(chainName);
        if (optionalBlockchainData.isEmpty()) return Optional.empty();
        return Optional.of(BlockchainDataResponse.build(optionalBlockchainData.get()));
    }

    public ResponseEntity<Object> changeBlockchain(BlockchainData blockchainData) {
        if (!blockchainsRepository.existsByUrlOrName(blockchainData.url, blockchainData.name)) return ResponseHandler.generateResponse(ErrorMessage.BLOCKCHAIN_ALREADY_ADDED , HttpStatus.BAD_REQUEST, null);
        blockchainsRepository.save(blockchainData);
        return ResponseHandler.generateResponse(null , HttpStatus.BAD_REQUEST, true);
    }

    public boolean isWalletOwnerOfNftByIndex(String username, long nftIndex, String chainName) {
        Optional<BlockchainData> blockchainDataOptional = blockchainsRepository.findByName(chainName);
        if (blockchainDataOptional.isEmpty()) return false;
        BlockchainData blockchainData = blockchainDataOptional.get();
        StandardContractProvider standardContractProvider = new StandardContractProvider(blockchainData.url, blockchainData.getPrivateKey());
        try {
            Optional<ConnectedWallet> optionalConnectedWallet = connectedWalletsRepository.findByUsername(username);
            if (optionalConnectedWallet.isEmpty()) return false;
            return standardContractProvider.getERC721Token(blockchainData.nftContractAddress).ownerOf(BigInteger.valueOf(nftIndex)).send().equalsIgnoreCase(optionalConnectedWallet.get().getAddress());
        } catch (Exception e) {
            logger.error(e.getMessage());
            return false;
        }
    }
}