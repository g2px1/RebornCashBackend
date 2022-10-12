package com.client.eurekaclient.services.web3;

import com.client.eurekaclient.messages.ErrorMessage;
import com.client.eurekaclient.models.DTO.transactions.TransactionResult;
import com.client.eurekaclient.models.DTO.users.User;
import com.client.eurekaclient.models.response.BlockchainDataResponse;
import com.client.eurekaclient.models.response.ResponseHandler;
import com.client.eurekaclient.models.web3.BlockchainData;
import com.client.eurekaclient.models.web3.ConnectedWallet;
import com.client.eurekaclient.models.web3.Transaction;
import com.client.eurekaclient.repositories.BlockchainsRepository;
import com.client.eurekaclient.repositories.ConnectedWalletsRepository;
import com.client.eurekaclient.repositories.TransactionsRepository;
import com.client.eurekaclient.services.lock.FairLock;
import com.client.eurekaclient.services.openfeign.users.UserInterface;
import com.client.eurekaclient.services.web3.contracts.OreChainContractProvider;
import com.client.eurekaclient.services.web3.contracts.StandardContractProvider;
import com.client.eurekaclient.services.web3.gas.GasProvider;
import com.client.eurekaclient.utilities.password.generator.RandomPasswordGenerator;
import com.j256.twofactorauth.TimeBasedOneTimePasswordUtil;
import com.jayway.jsonpath.JsonPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Transfer;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.utils.Convert;

import java.io.File;
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
    @Value("${app.path.to.wallets}")
    private String pathToWalletFiles;

    public Optional<TransactionResult> sendNativeTokenTransaction(String recipientAddress, String chainName, double amount, String username) {
        Optional<BlockchainData> blockchainDataOptional = blockchainsRepository.findByName(chainName);
        if (blockchainDataOptional.isEmpty()) return Optional.of(new TransactionResult(null, true, ErrorMessage.CHAIN_NOT_SUPPORTED));
        BlockchainData blockchainData = blockchainDataOptional.get();
        amount = Web3Service.rounder.apply(amount);
        Credentials credentials = null;
        try {
            credentials = WalletUtils.loadCredentials(
                    pathToWalletFiles,
                    this.pathToWalletFiles);
        } catch (java.io.IOException | org.web3j.crypto.CipherException e) {
            logger.error(e.getMessage());
            return Optional.empty();
        }
        GasProvider gasProvider = new GasProvider(blockchainData.url);
        try {
            TransactionReceipt transactionReceipt = Transfer.sendFunds(
                    gasProvider.getWeb3j(), credentials, recipientAddress,
                    BigDecimal.valueOf(amount), Convert.Unit.ETHER).send();
            if (transactionsRepository.existsByHashAndChainName(transactionReceipt.getTransactionHash(), chainName)) return Optional.empty();
            transactionsRepository.save(new Transaction(transactionReceipt.getTransactionHash(), chainName, username));
            return Optional.of(new TransactionResult(transactionReceipt, false, null));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return Optional.of(new TransactionResult(null, true, ErrorMessage.TRANSACTION_ERROR));
        }
    }
    public TransactionResult sendSafeNativeTokenTransaction(String recipientAddress, String chainName, double amount, String username, String code) {
        User user = userInterface.getUser(username).get();
        try {
            if (!TimeBasedOneTimePasswordUtil.validateCurrentNumber(user.getSecretKey(), Integer.parseInt(code), 0))
                return new TransactionResult(null, true, ErrorMessage.INVALID_CODE);
        } catch (GeneralSecurityException e) {
            logger.error(e.getMessage());
            return new TransactionResult(null, true, ErrorMessage.DEFAULT_ERROR);
        }
        Optional<BlockchainData> blockchainDataOptional = blockchainsRepository.findByName(chainName);
        if (blockchainDataOptional.isEmpty()) return new TransactionResult(null, true, ErrorMessage.CHAIN_NOT_SUPPORTED);
        BlockchainData blockchainData = blockchainDataOptional.get();
        amount = Web3Service.rounder.apply(amount);
        Credentials credentials = null;
        try {
            credentials = WalletUtils.loadCredentials(
                    blockchainData.passwordToWalletFile,
                    this.pathToWalletFiles+blockchainData.walletFileName);
        } catch (java.io.IOException | org.web3j.crypto.CipherException e) {
            logger.error(e.getMessage());
            return new TransactionResult(null, true, ErrorMessage.DEFAULT_ERROR);
        }
        GasProvider gasProvider = new GasProvider(blockchainData.url);
        try {
            TransactionReceipt transactionReceipt = Transfer.sendFunds(
                    gasProvider.getWeb3j(), credentials, recipientAddress,
                    BigDecimal.valueOf(amount), Convert.Unit.ETHER).send();
            if (transactionsRepository.existsByHashAndChainName(transactionReceipt.getTransactionHash(), chainName)) return new TransactionResult(null, true, String.format(ErrorMessage.TRANSACTION_ERROR, "transaction already exists."));
            transactionsRepository.save(new Transaction(transactionReceipt.getTransactionHash(), chainName, username));
            user.setBalance(user.getBalance().subtract(BigDecimal.valueOf(amount)));
            userInterface.saveUser(user);
            return new TransactionResult(transactionReceipt, false, null);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new TransactionResult(null, true, ErrorMessage.TRANSACTION_ERROR);
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
        String password = RandomPasswordGenerator.generateRandomPassword();
        String fileName = null;
        try {
            fileName = WalletUtils.generateNewWalletFile(
                    password,
                    new File(this.pathToWalletFiles));
        } catch (org.web3j.crypto.CipherException | java.security.InvalidAlgorithmParameterException | java.security.NoSuchAlgorithmException | java.security.NoSuchProviderException | java.io.IOException e) {
            logger.error(e.getMessage());
            return ResponseHandler.generateResponse(ErrorMessage.DEFAULT_ERROR , HttpStatus.OK, null);
        }
        Credentials credentials = null;
        try {
            credentials = WalletUtils.loadCredentials(password, this.pathToWalletFiles+fileName);
        } catch (java.io.IOException | org.web3j.crypto.CipherException e) {
            logger.error(e.getMessage());
            return ResponseHandler.generateResponse(ErrorMessage.DEFAULT_ERROR , HttpStatus.OK, null);
        }
        if (blockchainsRepository.existsByUrlOrName(blockchainData.url, blockchainData.name.toLowerCase(Locale.ROOT))) return ResponseHandler.generateResponse(ErrorMessage.BLOCKCHAIN_ALREADY_ADDED , HttpStatus.BAD_REQUEST, null);
        blockchainData.setPasswordToWalletFile(password);
        blockchainData.setWalletFileName(fileName);
        blockchainData.setHotWalletAddress(credentials.getAddress());
        blockchainsRepository.save(blockchainData);
        return ResponseHandler.generateResponse(null , HttpStatus.OK, true);
    }

    public Optional<BlockchainDataResponse> getBlockchain(String chainName) {
        Optional<BlockchainData> optionalBlockchainData = blockchainsRepository.findByName(chainName);
        if (optionalBlockchainData.isEmpty()) return Optional.empty();
        return BlockchainDataResponse.build(optionalBlockchainData.get(), pathToWalletFiles);
    }

    public ResponseEntity<Object> getAddress(String chainName) {
        Optional<BlockchainData> blockchainData = blockchainsRepository.findByName(chainName);
        if(blockchainData.isEmpty()) return ResponseHandler.generateResponse(ErrorMessage.CHAIN_NOT_SUPPORTED, HttpStatus.BAD_REQUEST, null);
        return ResponseHandler.generateResponse(blockchainData.get().hotWalletAddress, HttpStatus.OK, null);
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
        OreChainContractProvider oreChainContractProvider = new OreChainContractProvider(blockchainData.url, blockchainData.getPrivateKey());
        try {
            Optional<ConnectedWallet> optionalConnectedWallet = connectedWalletsRepository.findByUsername(username);
            if (optionalConnectedWallet.isEmpty()) return false;
            return oreChainContractProvider.getERC721Token(blockchainData.nftContractAddress).ownerOf(BigInteger.valueOf(nftIndex)).send().equalsIgnoreCase(optionalConnectedWallet.get().getAddress());
        } catch (Exception e) {
            logger.error(e.getMessage());
            return false;
        }
    }
}