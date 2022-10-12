package com.client.eurekaclient.services.rabbithunt.converter;

import com.client.eurekaclient.messages.ErrorMessage;
import com.client.eurekaclient.models.DTO.transactions.TransactionResult;
import com.client.eurekaclient.models.DTO.users.User;
import com.client.eurekaclient.models.lock.UserLock;
import com.client.eurekaclient.models.nft.NFT;
import com.client.eurekaclient.models.request.NFT.NFTSeekingRequest;
import com.client.eurekaclient.models.request.goldenrush.token.ConvertingTokenRequest;
import com.client.eurekaclient.models.request.goldenrush.token.InvestmentInBurgerRequest;
import com.client.eurekaclient.models.request.unit.TransferTokensRequests;
import com.client.eurekaclient.models.request.web3.ConnectedWallet;
import com.client.eurekaclient.models.request.web3.TransactionRequest;
import com.client.eurekaclient.models.response.ResponseHandler;
import com.client.eurekaclient.models.scheduled.transactions.ScheduledTransaction;
import com.client.eurekaclient.repositories.ScheduledTransactionRepository;
import com.client.eurekaclient.services.lock.FairLock;
import com.client.eurekaclient.services.openfeign.NFT.NFTInterface;
import com.client.eurekaclient.services.openfeign.blockchain.BlockchainInterface;
import com.client.eurekaclient.services.openfeign.transactions.unit.UnitInterface;
import com.client.eurekaclient.services.openfeign.users.UserInterface;
import com.client.eurekaclient.services.openfeign.wallets.BalanceInterface;
import com.client.eurekaclient.services.openfeign.wallets.ConnectedWalletInterface;
import com.client.eurekaclient.services.rabbithunt.transaction.ScheduledTxService;
import com.client.eurekaclient.services.rabbithunt.mine.UserMineService;
import com.client.eurekaclient.services.unit.UnitService;
import com.client.eurekaclient.utilities.http.finance.BinanceRequest;
import com.j256.twofactorauth.TimeBasedOneTimePasswordUtil;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.GeneralSecurityException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class TokensConverter {
    @Autowired
    private UserInterface userInterface;
    @Autowired
    private UnitInterface unitInterface;
    @Autowired
    private UnitService unitService;
    @Autowired
    private NFTInterface nftInterface;
    @Autowired
    private ConnectedWalletInterface connectedWalletInterface;
    @Autowired
    private BalanceInterface balanceInterface;
    @Autowired
    private ScheduledTransactionRepository scheduledTransactionRepository;
    @Autowired
    private ScheduledTxService scheduledTxService;
    @Autowired
    private BlockchainInterface blockchainInterface;
    @Autowired
    private FairLock fairLock;
    private static final Logger logger = LoggerFactory.getLogger(UserMineService.class);

    public ResponseEntity<Object> convertLayer1TokensIntoGame(ConvertingTokenRequest convertingTokenRequest, String username) {
        if (convertingTokenRequest.tokenName.equalsIgnoreCase("silver_coin") || convertingTokenRequest.tokenName.equalsIgnoreCase("gold_coin")) {
            Optional<UserLock> userLock = fairLock.getUserFairLock(username);
            if(userLock.isEmpty()) return ResponseHandler.generateResponse(ErrorMessage.LOCK, HttpStatus.OK, null);
            Optional<NFT> optionalNFT = nftInterface.findByIndex(new NFTSeekingRequest(convertingTokenRequest.nftIndex));
            if (optionalNFT.isEmpty()) {
                fairLock.unlockUserLock(username);
                return ResponseHandler.generateResponse(ErrorMessage.NFT_NOT_EXISTS, HttpStatus.BAD_REQUEST, null);
            }
            Optional<ConnectedWallet> optionalConnectedWallet = connectedWalletInterface.findByUsername(username);
            if (optionalConnectedWallet.isEmpty()) {
                fairLock.unlockUserLock(username);
                return ResponseHandler.generateResponse(ErrorMessage.METAMASK_ERROR, HttpStatus.BAD_REQUEST, null);
            }
            NFT nft = optionalNFT.get();
            if (balanceInterface.isOwnerOfNFT(username, nft.index, convertingTokenRequest.chainName)) {
                fairLock.unlockUserLock(username);
                return ResponseHandler.generateResponse(ErrorMessage.DEFAULT_ERROR, HttpStatus.BAD_REQUEST, null);
            }
            Optional<JSONObject> optionalBalance = Optional.ofNullable(unitService.getBalance(nft.name));
            if (optionalBalance.isEmpty()) {
                fairLock.unlockUserLock(username);
                return ResponseHandler.generateResponse(ErrorMessage.BALANCE_NOT_FOUND, HttpStatus.BAD_REQUEST, null);
            }
            Optional<Double> balance = getValueInDouble(optionalBalance.get(), nft.name);
            if (balance.isEmpty()) {
                fairLock.unlockUserLock(username);
                return ResponseHandler.generateResponse(ErrorMessage.BALANCE_NOT_FOUND, HttpStatus.BAD_REQUEST, null);
            }
            if (balance.get() < convertingTokenRequest.tokenAmount) {
                fairLock.unlockUserLock(username);
                return ResponseHandler.generateResponse(ErrorMessage.LOW_TOKEN_BALANCE, HttpStatus.BAD_REQUEST, null);
            }
            User user = userInterface.getUser(username).get();
            if (!user.isTwoFA()) {
                fairLock.unlockUserLock(username);
                return ResponseHandler.generateResponse(ErrorMessage.NEED_TO_BE_2FA, HttpStatus.OK, null);
            }
            try {
                if (!TimeBasedOneTimePasswordUtil.validateCurrentNumber(user.getSecretKey(), Integer.parseInt(convertingTokenRequest.code), 0)) {
                    fairLock.unlockUserLock(username);
                    return ResponseHandler.generateResponse(ErrorMessage.INVALID_CODE, HttpStatus.OK, null);
                }
            } catch (GeneralSecurityException e) {
                fairLock.unlockUserLock(username);
                logger.error(e.getMessage());
                return ResponseHandler.generateResponse(ErrorMessage.DEFAULT_ERROR, HttpStatus.BAD_REQUEST, null);
            }
            if (user.getBalance().compareTo(BigDecimal.valueOf(10)) < 0) {
                fairLock.unlockUserLock(username);
                return ResponseHandler.generateResponse(ErrorMessage.LOW_NATIVE_TOKENS_BALANCE, HttpStatus.OK, null);
            }

            double tokensQuantityToUSD = (convertingTokenRequest.tokenName.equalsIgnoreCase("silver_coin")) ? convertingTokenRequest.tokenAmount * 0.01 : convertingTokenRequest.tokenAmount;
            double bnbAmount = tokensQuantityToUSD * (1/ BinanceRequest.getBNBUSDtPrice());

            JSONObject unitResponse = unitService.sendTokens(new TransferTokensRequests("merchant", nft.name, convertingTokenRequest.tokenAmount, convertingTokenRequest.tokenName));

            TransactionResult transactionResult = balanceInterface.sendNativeTokenTransaction(new TransactionRequest(convertingTokenRequest.chainName, username, bnbAmount));
            if (transactionResult.error) {
                fairLock.unlockUserLock(username);
                return ResponseHandler.generateResponse(transactionResult.errorMessage, HttpStatus.OK, null);
            }
            user.setBalance(user.getBalance().subtract(BigDecimal.valueOf(10)));
            userInterface.saveUser(user);

            if (convertingTokenRequest.tokenName.equalsIgnoreCase("gold_coin"))
                scheduledTxService.subtractTxByNft(nft.name, convertingTokenRequest.tokenAmount);

            fairLock.unlockUserLock(username);
            return ResponseHandler.generateResponse("", HttpStatus.OK, new HashMap<>(Map.of("unitResponse", unitResponse.toMap(), "mainChainResponse", transactionResult.transactionReceipt.toString())));
        }
        return ResponseHandler.generateResponse(ErrorMessage.DEFAULT_ERROR, HttpStatus.OK, null);
    }

    public ResponseEntity<Object> investInBurger(InvestmentInBurgerRequest investmentInBurgerRequest, String username) {
        Optional<UserLock> userLock = fairLock.getUserFairLock(username);
        if (userLock.isEmpty()) return ResponseHandler.generateResponse(ErrorMessage.LOCK, HttpStatus.OK, null);
        if (investmentInBurgerRequest.quantityOfBurgers == 0) {
            fairLock.unlockUserLock(username);
            return ResponseHandler.generateResponse(ErrorMessage.BURGER_QUANTITY_ERROR, HttpStatus.OK, null);
        }
        Optional<NFT> optionalNFT = nftInterface.findByIndex(new NFTSeekingRequest(investmentInBurgerRequest.index));
        if (optionalNFT.isEmpty()) {
            fairLock.unlockUserLock(username);
            return ResponseHandler.generateResponse(ErrorMessage.NFT_NOT_EXISTS, HttpStatus.OK, null);
        }
        Optional<ConnectedWallet> optionalConnectedWallet = connectedWalletInterface.findByUsername(username);
        if (optionalConnectedWallet.isEmpty()) {
            fairLock.unlockUserLock(username);
            return ResponseHandler.generateResponse(ErrorMessage.METAMASK_ERROR, HttpStatus.OK, null);
        }
        if (balanceInterface.isOwnerOfNFT(username, investmentInBurgerRequest.index, investmentInBurgerRequest.chainName)) {
            fairLock.unlockUserLock(username);
            return ResponseHandler.generateResponse(ErrorMessage.OWNERSHIP_ERROR, HttpStatus.OK, null);
        }
        User user = userInterface.getUser(username).get();
        if (!user.isTwoFA()) {
            fairLock.unlockUserLock(username);
            return ResponseHandler.generateResponse(ErrorMessage.NEED_TO_BE_2FA, HttpStatus.OK, null);
        }
        try {
            if (!TimeBasedOneTimePasswordUtil.validateCurrentNumber(user.getSecretKey(), Integer.parseInt(investmentInBurgerRequest.code), 0)) {
                fairLock.unlockUserLock(username);
                return ResponseHandler.generateResponse(ErrorMessage.INVALID_CODE, HttpStatus.OK, null);
            }
        } catch (GeneralSecurityException e) {
            fairLock.unlockUserLock(username);
            logger.error(e.getMessage());
            return ResponseHandler.generateResponse(ErrorMessage.DEFAULT_ERROR, HttpStatus.BAD_REQUEST, null);
        }
        if (user.getBalance().compareTo(BigDecimal.valueOf(10)) < 0) {
            fairLock.unlockUserLock(username);
            return ResponseHandler.generateResponse(ErrorMessage.LOW_GOLD_COINS_BALANCE, HttpStatus.BAD_REQUEST, null);
        }
        NFT nft = optionalNFT.get();
        Optional<List<ScheduledTransaction>> optionalScheduledTransactionList = scheduledTransactionRepository.findByNftNameAndActiveTillLessThanAndReverted(nft.name, new Date().getTime(), false);
        optionalScheduledTransactionList.ifPresent(layer1ExpiringTransactionsList -> { // check if list !empty
            layer1ExpiringTransactionsList.forEach(scheduledTransaction -> { // looking for all Transactions and their amounts
                unitService.sendTokens(new TransferTokensRequests("merchant", nft.name, scheduledTransaction.amount, scheduledTransaction.tokenName));
                scheduledTransaction.setReverted(true); // setting status of transaction
            });
            scheduledTransactionRepository.saveAll(optionalScheduledTransactionList.get()); // saving changes if exists
        });

        Optional<JSONObject> optionalJsonBalance = Optional.ofNullable(unitService.getBalance(nft.name));
        if (optionalJsonBalance.isEmpty()) {
            fairLock.unlockUserLock(username);
            return ResponseHandler.generateResponse(ErrorMessage.DEFAULT_ERROR, HttpStatus.OK, null);
        }
        Optional<Double> optionalInCarrotsBalance = UserMineService.getValueInDouble(optionalJsonBalance.get(), "gold_coin");
        if (optionalInCarrotsBalance.isEmpty()) {
            fairLock.unlockUserLock(username);
            return ResponseHandler.generateResponse(ErrorMessage.DEFAULT_ERROR, HttpStatus.OK, null);
        }
        double inGoldCoinsBalance = optionalInCarrotsBalance.get();
        if (inGoldCoinsBalance < investmentInBurgerRequest.quantityOfBurgers * 10) {
            fairLock.unlockUserLock(username);
            return ResponseHandler.generateResponse(ErrorMessage.LOW_GOLD_COINS_BALANCE, HttpStatus.OK, null);
        }

        JSONObject goldCoin = unitService.sendTokens(new TransferTokensRequests("merchant", nft.name, investmentInBurgerRequest.quantityOfBurgers * 10, "gold_coin"));
        JSONObject inIngot = unitService.sendTokens(new TransferTokensRequests(nft.name, "merchant", investmentInBurgerRequest.quantityOfBurgers, "gold_ingot"));
        user.setBalance(user.getBalance().subtract(BigDecimal.valueOf(10)));
        userInterface.saveUser(user);
        scheduledTxService.subtractTxByNft(nft.name, investmentInBurgerRequest.quantityOfBurgers * 10);
        fairLock.unlockUserLock(username);
        return ResponseHandler.generateResponse("", HttpStatus.OK, new HashMap<>(Map.of("goldCoinTransactionHash", goldCoin.toMap(), "goldIngotTransactionHash", inIngot.toMap())));
    }

    private static Optional<Double> getValueInDouble(JSONObject balance, String tokenName) {
        AtomicReference<JSONObject> balanceOptional = new AtomicReference<>();
        balance.getJSONObject("balance").getJSONArray("tokens_balance").forEach(tokens -> {
            JSONObject jsonObject = new JSONObject(tokens.toString());
            if (jsonObject.has(tokenName.toLowerCase(Locale.ROOT)))
                balanceOptional.set(jsonObject);
        });
        double inTokenBalance;
        try {
            inTokenBalance = Double.parseDouble(balanceOptional.get().get(tokenName).toString());
        } catch (Exception e) {
            return Optional.empty();
        }
        return Optional.of(inTokenBalance);
    }
}
