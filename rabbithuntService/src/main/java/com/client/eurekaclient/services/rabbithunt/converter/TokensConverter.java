package com.client.eurekaclient.services.rabbithunt.converter;

import com.client.eurekaclient.messages.ErrorMessage;
import com.client.eurekaclient.models.DTO.transactions.TransactionResult;
import com.client.eurekaclient.models.DTO.users.User;
import com.client.eurekaclient.models.lock.UserLock;
import com.client.eurekaclient.models.nft.NFT;
import com.client.eurekaclient.models.request.NFT.NFTSeekingRequest;
import com.client.eurekaclient.models.request.rabbithunt.token.ConvertingTokenRequest;
import com.client.eurekaclient.models.request.unit.TransferTokensRequests;
import com.client.eurekaclient.models.request.web3.ConnectedWallet;
import com.client.eurekaclient.models.request.web3.TransactionRequest;
import com.client.eurekaclient.models.response.ResponseHandler;
import com.client.eurekaclient.services.lock.FairLock;
import com.client.eurekaclient.services.openfeign.NFT.NFTInterface;
import com.client.eurekaclient.services.openfeign.blockchain.BlockchainInterface;
import com.client.eurekaclient.services.openfeign.transactions.unit.UnitInterface;
import com.client.eurekaclient.services.openfeign.users.UserInterface;
import com.client.eurekaclient.services.openfeign.wallets.BalanceInterface;
import com.client.eurekaclient.services.openfeign.wallets.ConnectedWalletInterface;
import com.client.eurekaclient.services.rabbithunt.transaction.ScheduledTxService;
import com.client.eurekaclient.services.rabbithunt.trap.UserTrapService;
import com.client.eurekaclient.utilities.http.finance.BenSwapRequest;
import com.j256.twofactorauth.TimeBasedOneTimePasswordUtil;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class TokensConverter {
    @Autowired
    private UserInterface userInterface;
    @Autowired
    private UnitInterface unitInterface;
    @Autowired
    private NFTInterface nftInterface;
    @Autowired
    private ConnectedWalletInterface connectedWalletInterface;
    @Autowired
    private BalanceInterface balanceInterface;
    @Autowired
    private ScheduledTxService scheduledTxService;
    @Autowired
    private BlockchainInterface blockchainInterface;
    @Autowired
    private FairLock fairLock;
    private static final Logger logger = LoggerFactory.getLogger(UserTrapService.class);

    public ResponseEntity<Object> convertLayer1TokensIntoGame(ConvertingTokenRequest convertingTokenRequest, String username) {
        if (convertingTokenRequest.tokenName.equalsIgnoreCase("carrot") || convertingTokenRequest.tokenName.equalsIgnoreCase("meat")) {
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
            Optional<JSONObject> optionalBalance = balanceInterface.getBalance(nft.name);
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
                return ResponseHandler.generateResponse(ErrorMessage.LOW_GAME_BALANCE, HttpStatus.OK, null);
            }
            Optional<JSONArray> optionalGameData = BenSwapRequest.getGameData();
            if (optionalGameData.isEmpty()) {
                fairLock.unlockUserLock(username);
                return ResponseHandler.generateResponse(ErrorMessage.API_NOT_AVAILABLE, HttpStatus.OK, null);
            }

            double usdAmount = (convertingTokenRequest.tokenName.equalsIgnoreCase("carrot")) ? convertingTokenRequest.tokenAmount * 0.01 : convertingTokenRequest.tokenAmount;

            JSONObject unitResponse = unitInterface.sendTokens(new TransferTokensRequests("merchant", nft.name, convertingTokenRequest.tokenAmount, convertingTokenRequest.tokenName));

            TransactionResult transactionResult = balanceInterface.sendStableCoins(new TransactionRequest(convertingTokenRequest.chainName, username, usdAmount));
            if (transactionResult.error) {
                fairLock.unlockUserLock(username);
                return ResponseHandler.generateResponse(transactionResult.errorMessage, HttpStatus.OK, null);
            }
            user.setBalance(user.getBalance().subtract(BigDecimal.valueOf(10)));
            userInterface.saveUser(user);

            balanceInterface.sendIMMOGame(new TransactionRequest(convertingTokenRequest.chainName, username, 10));

            if (convertingTokenRequest.tokenName.equalsIgnoreCase("meat"))
                scheduledTxService.subtractTxByNft(nft.name, convertingTokenRequest.tokenAmount);

            fairLock.unlockUserLock(username);
            return ResponseHandler.generateResponse("", HttpStatus.OK, new HashMap<>(Map.of("unitResponse", unitResponse.toMap(), "mainChainResponse", transactionResult.transactionReceipt.toString())));
        }
        return ResponseHandler.generateResponse(ErrorMessage.DEFAULT_ERROR, HttpStatus.OK, null);
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
