package com.client.eurekaclient.services.rabbithunt.market.tokens;

import com.client.eurekaclient.constants.Constants;
import com.client.eurekaclient.messages.Errors;
import com.client.eurekaclient.models.DTO.users.User;
import com.client.eurekaclient.models.lock.UserLock;
import com.client.eurekaclient.models.market.goldenrush.Token;
import com.client.eurekaclient.models.nft.NFT;
import com.client.eurekaclient.models.request.NFT.NFTSeekingRequest;
import com.client.eurekaclient.models.request.market.ProductSeekingRequest;
import com.client.eurekaclient.models.request.merchant.BuyTokensFromMerchantRequest;
import com.client.eurekaclient.models.request.goldenrush.token.market.BuyTokensRequest;
import com.client.eurekaclient.models.request.goldenrush.token.market.SellTokensRequest;
import com.client.eurekaclient.models.request.unit.TransferTokensRequests;
import com.client.eurekaclient.models.response.ResponseHandler;
import com.client.eurekaclient.repositories.TokensRepository;
import com.client.eurekaclient.services.lock.FairLock;
import com.client.eurekaclient.services.openfeign.NFT.NFTInterface;
import com.client.eurekaclient.services.openfeign.market.MarketInterface;
import com.client.eurekaclient.services.openfeign.transactions.unit.UnitInterface;
import com.client.eurekaclient.services.openfeign.users.UserInterface;
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
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.security.GeneralSecurityException;
import java.util.*;

@Service
public class TokensService {
    @Autowired
    private MarketInterface marketInterface;
    @Autowired
    private TokensRepository tokensRepository;
    @Autowired
    private UserInterface userInterface;
    @Autowired
    private NFTInterface nftInterface;
    @Autowired
    private UnitInterface unitInterface;
    @Autowired
    private UnitService unitService;
    @Autowired
    private FairLock fairLock;
    @Autowired
    private Errors errors;
    private final Logger logger = LoggerFactory.getLogger(TokensService.class);

    public ResponseEntity<Object> sellToken(SellTokensRequest sellTokensRequest, String username) {
        Optional<UserLock> userLock = fairLock.getUserFairLock(username);
        if(userLock.isEmpty()) return ResponseHandler.generateResponse(errors.LOCK, HttpStatus.BAD_REQUEST, null);
        if (sellTokensRequest.price > Constants.GAME_SUPPLY) {
            fairLock.unlockUserLock(username);
            return ResponseHandler.generateResponse(errors.AMOUNT_IS_TOO_LARGE, HttpStatus.BAD_REQUEST, null);
        }
//        if (!tokensRepository.existsByName(sellTokensRequest.tokenName.toLowerCase(Locale.ROOT)) || !sellTokensRequest.tokenName.equalsIgnoreCase("golden_ingot")) return ResponseHandler.generateResponse(errors.DEFAULT_ERROR, HttpStatus.BAD_REQUEST, null);
        Optional<NFT> optionalNFT = nftInterface.findByIndex(new NFTSeekingRequest(sellTokensRequest.NFTId));
        if (optionalNFT.isEmpty()) {
            fairLock.unlockUserLock(username);
            return ResponseHandler.generateResponse(errors.NFT_NOT_EXISTS, HttpStatus.BAD_REQUEST, null);
        }
        User user = userInterface.getUser(username).get();
        if (!user.isTwoFA()) {
            fairLock.unlockUserLock(username);
            return ResponseHandler.generateResponse(errors.NEED_TO_BE_2FA, HttpStatus.BAD_REQUEST, null);
        }
        try {
            if (!TimeBasedOneTimePasswordUtil.validateCurrentNumber(user.getSecretKey(), Integer.parseInt(sellTokensRequest.code), 0)) {
                fairLock.unlockUserLock(username);
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errors.INVALID_CODE);
            }
        } catch (GeneralSecurityException e) {
            logger.error(e.getMessage());
            fairLock.unlockUserLock(username);
            return ResponseHandler.generateResponse(errors.DEFAULT_ERROR, HttpStatus.BAD_REQUEST, null);
        }
        NFT nft = optionalNFT.get();
        JSONObject jsonObject = unitService.sendTokens(new TransferTokensRequests("merchant", nft.name, sellTokensRequest.amount, sellTokensRequest.tokenName));
        if(jsonObject.has("error")) {
            fairLock.unlockUserLock(username);
            return ResponseHandler.generateResponse(errors.LOW_TOKEN_BALANCE, HttpStatus.BAD_REQUEST, null);
        }
        fairLock.unlockUserLock(username);
        marketInterface.save(new Token(sellTokensRequest.price, username, sellTokensRequest.amount, new Date().getTime(), null, String.format("token: %s", sellTokensRequest.tokenName.toLowerCase(Locale.ROOT)), true, UUID.randomUUID().toString(), nft.name, sellTokensRequest.tokenName));
        return ResponseHandler.generateResponse(null, HttpStatus.OK, new HashMap<>(Map.of("outTokenUnitResponse", jsonObject.get("hash"))));
    }

    public ResponseEntity<Object> buyToken(BuyTokensRequest buyTokensRequest, String username) {
        Optional<UserLock> userLock = fairLock.getUserFairLock(username);
        if(userLock.isEmpty()) return ResponseHandler.generateResponse(errors.LOCK, HttpStatus.OK, null);
        User user = userInterface.getUser(username).get();
        if(!user.isTwoFA()) {
            fairLock.unlockUserLock(username);
            return ResponseHandler.generateResponse(errors.NEED_TO_BE_2FA, HttpStatus.OK, null);
        }
        Optional<Token> optionalToken = marketInterface.findAbstractByUuidAndStatus(new ProductSeekingRequest(buyTokensRequest.uuid, true));
        if(optionalToken.isEmpty()) {
            fairLock.unlockUserLock(username);
            return ResponseHandler.generateResponse(errors.PRODUCT_NOT_FOUND, HttpStatus.OK, null);
        }
        try {
            if (!TimeBasedOneTimePasswordUtil.validateCurrentNumber(user.getSecretKey(), Integer.parseInt(buyTokensRequest.code), 0)) {
                fairLock.unlockUserLock(username);
                return ResponseHandler.generateResponse(errors.INVALID_CODE, HttpStatus.OK, null);
            }
        } catch (GeneralSecurityException e) {
            logger.error(e.getMessage());
            fairLock.unlockUserLock(username);
            return ResponseHandler.generateResponse(errors.DEFAULT_ERROR, HttpStatus.OK, null);
        }
        Token token = optionalToken.get();
        Optional<NFT> optionalNFT = nftInterface.findNftByName(new NFTSeekingRequest(token.nftName));
        if (optionalNFT.isEmpty()) {
            fairLock.unlockUserLock(username);
            return ResponseHandler.generateResponse(errors.NFT_NOT_EXISTS, HttpStatus.OK, null);
        }
        NFT nft = optionalNFT.get();
        if (user.getBalance().compareTo(BigDecimal.valueOf(token.price)) < 0) return ResponseHandler.generateResponse(errors.LOW_NATIVE_TOKENS_BALANCE, HttpStatus.OK, null);
        JSONObject inTransaction = unitService.sendTokens(new TransferTokensRequests(nft.name, "merchant", token.quantity, token.tokenName));
        if(inTransaction.has("error")) {
            fairLock.unlockUserLock(username);
            return ResponseHandler.generateResponse(errors.LOW_TOKEN_BALANCE, HttpStatus.BAD_REQUEST, null);
        }
        token.setStatus(false);
        marketInterface.save(token);
        User seller = userInterface.getUser(token.seller).get();
        user.setBalance(user.getBalance().subtract(BigDecimal.valueOf(token.price)));
        seller.setBalance(seller.getBalance().add(BigDecimal.valueOf(token.price)));
        userInterface.saveAll(List.of(user, seller));
        fairLock.unlockUserLock(username);
        return ResponseHandler.generateResponse(null, HttpStatus.OK, new HashMap<>(Map.of("inTokenUnitResponse", inTransaction.get("hash"))));
    }

    public ResponseEntity<Object> buySilverCoins(BuyTokensFromMerchantRequest buyTokensFromMerchantRequest, String username) {
        if (buyTokensFromMerchantRequest.tokenName.equalsIgnoreCase("silver_coin") || buyTokensFromMerchantRequest.tokenName.equalsIgnoreCase("gold_coin")) {
            Optional<UserLock> userLock = fairLock.getUserFairLock(username);
            if (userLock.isEmpty()) return ResponseHandler.generateResponse(errors.LOCK, HttpStatus.OK, null);
            User user = userInterface.getUser(username).get();
            if (!user.isTwoFA()) {
                fairLock.unlockUserLock(username);
                return ResponseHandler.generateResponse(errors.NEED_TO_BE_2FA, HttpStatus.OK, null);
            }
            try {
                if (!TimeBasedOneTimePasswordUtil.validateCurrentNumber(user.getSecretKey(), Integer.parseInt(buyTokensFromMerchantRequest.code), 0)) {
                    fairLock.unlockUserLock(username);
                    return ResponseHandler.generateResponse(errors.INVALID_CODE, HttpStatus.OK, null);
                }
            } catch (GeneralSecurityException e) {
                logger.error(e.getMessage());
                fairLock.unlockUserLock(username);
                return ResponseHandler.generateResponse(errors.DEFAULT_ERROR, HttpStatus.OK, null);
            }
            if (!buyTokensFromMerchantRequest.tokenName.equalsIgnoreCase("silver_coin")) {
                fairLock.unlockUserLock(username);
                return ResponseHandler.generateResponse(errors.INVALID_DATA, HttpStatus.OK, null);
            }
            if (user.getBalance().compareTo(BigDecimal.valueOf(buyTokensFromMerchantRequest.amount)) < 0) {
                fairLock.unlockUserLock(username);
                return ResponseHandler.generateResponse(errors.LOW_NATIVE_TOKENS_BALANCE, HttpStatus.OK, null);
            }
            Optional<NFT> optionalNFT = nftInterface.findByIndex(new NFTSeekingRequest(buyTokensFromMerchantRequest.nftId));
            if (optionalNFT.isEmpty()) {
                fairLock.unlockUserLock(username);
                return ResponseHandler.generateResponse(errors.NFT_NOT_EXISTS, HttpStatus.OK, null);
            }
            double tokensQuantityToUSD = (buyTokensFromMerchantRequest.tokenName.equalsIgnoreCase("silver_coin")) ? buyTokensFromMerchantRequest.amount * 0.01 : buyTokensFromMerchantRequest.amount;
            double bnbAmount = tokensQuantityToUSD * (1 / BinanceRequest.getBNBUSDtPrice());
            BigDecimal newBalance = user.getBalance().subtract(BigDecimal.valueOf(bnbAmount));
            user.setBalance(newBalance);
            userInterface.saveUser(user);
            unitService.sendTokens(new TransferTokensRequests(optionalNFT.get().name, "merchant", buyTokensFromMerchantRequest.amount, buyTokensFromMerchantRequest.tokenName));
        }
        return ResponseHandler.generateResponse(errors.INVALID_DATA, HttpStatus.OK, null);
    }
}
