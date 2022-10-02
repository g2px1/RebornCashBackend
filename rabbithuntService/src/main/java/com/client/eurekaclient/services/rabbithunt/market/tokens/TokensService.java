package com.client.eurekaclient.services.rabbithunt.market.tokens;

import com.client.eurekaclient.constants.Constants;
import com.client.eurekaclient.messages.ErrorMessage;
import com.client.eurekaclient.models.DTO.users.User;
import com.client.eurekaclient.models.lock.UserLock;
import com.client.eurekaclient.models.market.rabbithunt.CellsPack;
import com.client.eurekaclient.models.market.rabbithunt.Token;
import com.client.eurekaclient.models.nft.NFT;
import com.client.eurekaclient.models.request.NFT.NFTSeekingRequest;
import com.client.eurekaclient.models.request.market.ProductSeekingRequest;
import com.client.eurekaclient.models.request.rabbithunt.token.market.BuyTokensRequest;
import com.client.eurekaclient.models.request.rabbithunt.token.market.SellTokensRequest;
import com.client.eurekaclient.models.request.unit.TransferTokensRequests;
import com.client.eurekaclient.models.response.ResponseHandler;
import com.client.eurekaclient.repositories.TokensRepository;
import com.client.eurekaclient.services.lock.FairLock;
import com.client.eurekaclient.services.openfeign.NFT.NFTInterface;
import com.client.eurekaclient.services.openfeign.market.MarketInterface;
import com.client.eurekaclient.services.openfeign.transactions.unit.UnitInterface;
import com.client.eurekaclient.services.openfeign.users.UserInterface;
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
    private FairLock fairLock;
    private final Logger logger = LoggerFactory.getLogger(TokensService.class);

    public ResponseEntity<Object> sellToken(SellTokensRequest sellTokensRequest, String username) {
        Optional<UserLock> userLock = fairLock.getUserFairLock(username);
        if(userLock.isEmpty()) return ResponseHandler.generateResponse(ErrorMessage.LOCK, HttpStatus.OK, null);
        if (sellTokensRequest.price > Constants.GAME_SUPPLY) {
            fairLock.unlockUserLock(username);
            return ResponseHandler.generateResponse(ErrorMessage.AMOUNT_IS_TOO_LARGE, HttpStatus.OK, null);
        }
        if (!tokensRepository.existsByName(sellTokensRequest.tokenName.toLowerCase(Locale.ROOT)) || !sellTokensRequest.tokenName.equalsIgnoreCase("burger")) return ResponseHandler.generateResponse(ErrorMessage.DEFAULT_ERROR, HttpStatus.OK, null);
        Optional<NFT> optionalNFT = nftInterface.findNftByName(new NFTSeekingRequest(sellTokensRequest.NFTId));
        if (optionalNFT.isEmpty()) {
            fairLock.unlockUserLock(username);
            return ResponseHandler.generateResponse(ErrorMessage.NFT_NOT_EXISTS, HttpStatus.OK, null);
        }
        User user = userInterface.getUser(username).get();
        if (!user.isTwoFA()) {
            fairLock.unlockUserLock(username);
            return ResponseHandler.generateResponse(ErrorMessage.NEED_TO_BE_2FA, HttpStatus.OK, null);
        }
        try {
            if (!TimeBasedOneTimePasswordUtil.validateCurrentNumber(user.getSecretKey(), Integer.parseInt(sellTokensRequest.code), 0)) {
                fairLock.unlockUserLock(username);
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessage.INVALID_CODE);
            }
        } catch (GeneralSecurityException e) {
            logger.error(e.getMessage());
            fairLock.unlockUserLock(username);
            return ResponseHandler.generateResponse(ErrorMessage.DEFAULT_ERROR, HttpStatus.OK, null);
        }
        NFT nft = optionalNFT.get();
        JSONObject jsonObject = unitInterface.sendTokens(new TransferTokensRequests("merchant", nft.name, sellTokensRequest.amount, sellTokensRequest.tokenName));
        fairLock.unlockUserLock(username);
        marketInterface.save(new Token(sellTokensRequest.price, username, sellTokensRequest.amount, new Date().getTime(), null, String.format("token: %s", sellTokensRequest.tokenName.toLowerCase(Locale.ROOT)), true, UUID.randomUUID().toString(), nft.name, sellTokensRequest.tokenName));
        return ResponseHandler.generateResponse(null, HttpStatus.OK, new HashMap<>(Map.of("outTokenUnitResponse", jsonObject.get("hash"))));
    }

    public ResponseEntity<Object> buyToken(BuyTokensRequest buyTokensRequest, String username) {
        Optional<UserLock> userLock = fairLock.getUserFairLock(username);
        User user = userInterface.getUser(username).get();
        if(!user.isTwoFA()) {
            fairLock.unlockUserLock(username);
            return ResponseHandler.generateResponse(ErrorMessage.NEED_TO_BE_2FA, HttpStatus.OK, null);
        }
        Optional<Token> optionalToken = marketInterface.findAbstractByUuidAndStatus(new ProductSeekingRequest(buyTokensRequest.uuid, true));
        if(optionalToken.isEmpty()) {
            fairLock.unlockUserLock(username);
            return ResponseHandler.generateResponse(ErrorMessage.PRODUCT_NOT_FOUND, HttpStatus.OK, null);
        }
        try {
            if (!TimeBasedOneTimePasswordUtil.validateCurrentNumber(user.getSecretKey(), Integer.parseInt(buyTokensRequest.code), 0)) {
                fairLock.unlockUserLock(username);
                return ResponseHandler.generateResponse(ErrorMessage.INVALID_CODE, HttpStatus.OK, null);
            }
        } catch (GeneralSecurityException e) {
            logger.error(e.getMessage());
            fairLock.unlockUserLock(username);
            return ResponseHandler.generateResponse(ErrorMessage.DEFAULT_ERROR, HttpStatus.OK, null);
        }
        Token token = optionalToken.get();
        Optional<NFT> optionalNFT = nftInterface.findNftByName(new NFTSeekingRequest(token.nftName));
        if (optionalNFT.isEmpty()) {
            fairLock.unlockUserLock(username);
            return ResponseHandler.generateResponse(ErrorMessage.NFT_NOT_EXISTS, HttpStatus.OK, null);
        }
        NFT nft = optionalNFT.get();
        if (user.getBalance().compareTo(BigDecimal.valueOf(token.price)) < 0) return ResponseHandler.generateResponse(ErrorMessage.LOW_GAME_BALANCE, HttpStatus.OK, null);
        JSONObject inTransaction = unitInterface.sendTokens(new TransferTokensRequests(nft.name, "merchant", token.quantity, token.tokenName));
        token.setStatus(false);
        marketInterface.save(token);
        User seller = userInterface.getUser(token.seller).get();
        user.setBalance(user.getBalance().subtract(BigDecimal.valueOf(token.price)));
        seller.setBalance(seller.getBalance().add(BigDecimal.valueOf(token.price)));
        userInterface.saveAll(List.of(user, seller));
        fairLock.unlockUserLock(username);
        return ResponseHandler.generateResponse(null, HttpStatus.OK, new HashMap<>(Map.of("inTokenUnitResponse", inTransaction.get("hash"))));
    }
}
