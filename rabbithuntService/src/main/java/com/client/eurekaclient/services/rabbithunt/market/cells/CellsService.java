package com.client.eurekaclient.services.rabbithunt.market.cells;

import com.client.eurekaclient.messages.Errors;
import com.client.eurekaclient.models.DTO.users.User;
import com.client.eurekaclient.models.lock.CellsLock;
import com.client.eurekaclient.models.lock.UserLock;
import com.client.eurekaclient.models.market.goldenrush.CellsPack;
import com.client.eurekaclient.models.nft.NFT;
import com.client.eurekaclient.models.goldenrush.mine.Mine;
import com.client.eurekaclient.models.request.NFT.NFTSeekingRequest;
import com.client.eurekaclient.models.request.cells.BuyCellsPacks;
import com.client.eurekaclient.models.request.cells.SellCellsRequest;
import com.client.eurekaclient.models.request.cells.WithdrawCellsPacks;
import com.client.eurekaclient.models.request.market.ProductSeekingRequest;
import com.client.eurekaclient.models.request.unit.TransferTokensRequests;
import com.client.eurekaclient.models.request.web3.ConnectedWallet;
import com.client.eurekaclient.models.response.ResponseHandler;
import com.client.eurekaclient.repositories.MineRepository;
import com.client.eurekaclient.services.lock.FairLock;
import com.client.eurekaclient.services.openfeign.NFT.NFTInterface;
import com.client.eurekaclient.services.openfeign.market.MarketInterface;
import com.client.eurekaclient.services.openfeign.transactions.unit.UnitInterface;
import com.client.eurekaclient.services.openfeign.users.UserInterface;
import com.client.eurekaclient.services.openfeign.wallets.BalanceInterface;
import com.client.eurekaclient.services.openfeign.wallets.ConnectedWalletInterface;
import com.client.eurekaclient.services.unit.UnitService;
import com.j256.twofactorauth.TimeBasedOneTimePasswordUtil;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class CellsService {
    @Autowired
    private BalanceInterface balanceInterface;
    @Autowired
    private UserInterface userInterface;
    @Autowired
    private ConnectedWalletInterface connectedWalletInterface;
    @Autowired
    private MineRepository mineRepository;
    @Autowired
    private NFTInterface nftInterface;
    @Autowired
    private UnitInterface unitInterface;
    @Autowired
    private UnitService unitService;
    @Autowired
    private MarketInterface marketInterface;
    @Autowired
    private Errors errors;
    @Autowired
    private FairLock fairLock;
    private static final Logger logger = LoggerFactory.getLogger(CellsPack.class);

    public ResponseEntity<Object> sellCells(SellCellsRequest sellCellsRequest, String username) {
        if(sellCellsRequest.cellsQuantity == 0) return ResponseHandler.generateResponse(errors.INVALID_DATA, HttpStatus.BAD_REQUEST, null);
        Optional<UserLock> userLock = fairLock.getUserFairLock(username);
        if(userLock.isEmpty()) return ResponseHandler.generateResponse(errors.LOCK, HttpStatus.BAD_REQUEST, null);
        try {
            if(sellCellsRequest.checkNull()) {
                fairLock.unlockUserLock(username);
                return ResponseHandler.generateResponse(errors.ALL_FIELDS_SHOULD_BE_FILLED_IN, HttpStatus.BAD_REQUEST, null);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            fairLock.unlockUserLock(username);
            return ResponseHandler.generateResponse(errors.INVALID_CODE, HttpStatus.BAD_REQUEST, null);
        }
        if(!mineRepository.existsByName(sellCellsRequest.trapName.toLowerCase(Locale.ROOT))) {
            fairLock.unlockUserLock(username);
            return ResponseHandler.generateResponse(errors.MINE_NOT_EXIST, HttpStatus.BAD_REQUEST, null);
        }
        Optional<ConnectedWallet> optionalConnectedWallet = connectedWalletInterface.findByUsername(username);
        if(optionalConnectedWallet.isEmpty()) {
            fairLock.unlockUserLock(username);
            return ResponseHandler.generateResponse(errors.METAMASK_ERROR, HttpStatus.BAD_REQUEST, null);
        }
        if(!balanceInterface.isOwnerOfNFT(username, sellCellsRequest.NFTId, sellCellsRequest.chainName)) {
            fairLock.unlockUserLock(username);
            return ResponseHandler.generateResponse(errors.OWNERSHIP_ERROR, HttpStatus.BAD_REQUEST, null);
        }
        User currentUser = userInterface.getUser(username).get();
        try {
            if(!TimeBasedOneTimePasswordUtil.validateCurrentNumber(currentUser.getSecretKey(), Integer.parseInt(sellCellsRequest.code), 0)) {
                fairLock.unlockUserLock(username);
                return ResponseHandler.generateResponse(errors.INVALID_CODE, HttpStatus.BAD_REQUEST, null);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            fairLock.unlockUserLock(username);
            return ResponseHandler.generateResponse(errors.DEFAULT_ERROR, HttpStatus.BAD_REQUEST, null);
        }
        if(!currentUser.isTwoFA()) {
            fairLock.unlockUserLock(username);
            return ResponseHandler.generateResponse(errors.NEED_TO_BE_2FA, HttpStatus.BAD_REQUEST, null);
        }
        if(sellCellsRequest.price > 100000000) {
            fairLock.unlockUserLock(username);
            return ResponseHandler.generateResponse(errors.AMOUNT_IS_TOO_LARGE, HttpStatus.BAD_REQUEST, null);
        }
        Mine mine = mineRepository.findByName(sellCellsRequest.trapName.toLowerCase(Locale.ROOT));
        if(!mine.status) {
            fairLock.unlockUserLock(username);
            return ResponseHandler.generateResponse(errors.MINE_CLOSED, HttpStatus.BAD_REQUEST, null);
        }
        Optional<NFT> optionalNFT = nftInterface.findByIndex(new NFTSeekingRequest(sellCellsRequest.NFTId));
        if(optionalNFT.isEmpty()) {
            fairLock.unlockUserLock(username);
            return ResponseHandler.generateResponse(errors.NFT_NOT_EXISTS, HttpStatus.BAD_REQUEST, null);
        }
        NFT nft = optionalNFT.get();
        Optional<JSONObject> jsonBalance = Optional.ofNullable(unitService.getBalance(nft.name));
        if(jsonBalance.isEmpty()) {
            fairLock.unlockUserLock(username);
            return ResponseHandler.generateResponse(errors.BALANCE_NOT_FOUND, HttpStatus.BAD_REQUEST, null);
        }
        Optional<Double> optionalTokenBalance = getValueInDouble(jsonBalance.get(), sellCellsRequest.trapName.toLowerCase(Locale.ROOT));
        if(optionalTokenBalance.isEmpty()) {
            fairLock.unlockUserLock(username);
            return ResponseHandler.generateResponse(errors.BALANCE_NOT_FOUND, HttpStatus.BAD_REQUEST, null);
        }
        if(optionalTokenBalance.get() < sellCellsRequest.cellsQuantity) {
            fairLock.unlockUserLock(username);
            return ResponseHandler.generateResponse(errors.LOW_TOKEN_BALANCE, HttpStatus.BAD_REQUEST, null);
        }
        JSONObject optionalJSONObject = unitService.sendTokens(new TransferTokensRequests("merchant", nft.name, sellCellsRequest.cellsQuantity, sellCellsRequest.trapName.toLowerCase(Locale.ROOT)));
        marketInterface.save(new CellsPack(sellCellsRequest.price, username, sellCellsRequest.cellsQuantity, new Date().getTime(), null, String.format("Cells from: %s", mine.name), true, UUID.randomUUID().toString(), nft.name, mine.name));
        fairLock.unlockUserLock(username);
        return ResponseHandler.generateResponse(null, HttpStatus.OK, null);
    }

    public ResponseEntity<Object> buyCellsPack(BuyCellsPacks buyCellsPacks, String username) {
        Optional<CellsLock> optionalCellsLock = fairLock.getCellsLock(buyCellsPacks.uuid);
        if(optionalCellsLock.isEmpty()) return ResponseHandler.generateResponse(errors.LOCK, HttpStatus.OK, null);
        Optional<UserLock> optionalUserLock = fairLock.getUserFairLock(username);
        if(optionalUserLock.isEmpty()) {
            fairLock.unlockCellsLock(buyCellsPacks.uuid);
            return ResponseHandler.generateResponse(errors.LOCK, HttpStatus.OK, null);
        }
        try {
            if(buyCellsPacks.checkNull()) return ResponseHandler.generateResponse(errors.ALL_FIELDS_SHOULD_BE_FILLED_IN, HttpStatus.OK, null);
        } catch (Exception e) {
            logger.error(e.getMessage());
            fairLock.unlockUserLock(username);
            fairLock.unlockCellsLock(buyCellsPacks.uuid);
            return ResponseHandler.generateResponse(errors.DEFAULT_ERROR, HttpStatus.OK, null);
        }
        Optional<ConnectedWallet> optionalConnectedWallet = connectedWalletInterface.findByUsername(username);
        if(optionalConnectedWallet.isEmpty()) {
            fairLock.unlockUserLock(username);
            fairLock.unlockCellsLock(buyCellsPacks.uuid);
            return ResponseHandler.generateResponse(errors.METAMASK_ERROR, HttpStatus.OK, null);
        }
        if(!balanceInterface.isOwnerOfNFT(username, buyCellsPacks.NFTId, buyCellsPacks.chainName)) {
            fairLock.unlockUserLock(username);
            fairLock.unlockCellsLock(buyCellsPacks.uuid);
            return ResponseHandler.generateResponse(errors.OWNERSHIP_ERROR, HttpStatus.OK, null);
        }
        Optional<CellsPack> optionalAbstractProduct = marketInterface.findAbstractByUuidAndStatus(new ProductSeekingRequest(buyCellsPacks.uuid));
        if (optionalAbstractProduct.isEmpty()) {
            fairLock.unlockUserLock(username);
            fairLock.unlockCellsLock(buyCellsPacks.uuid);
            return ResponseHandler.generateResponse(errors.METAMASK_ERROR, HttpStatus.OK, null);
        }
        CellsPack cellsPackToBuy = optionalAbstractProduct.get();

        User currentUser = userInterface.getUser(username).get();
        if(!currentUser.isTwoFA()) {
            fairLock.unlockUserLock(username);
            fairLock.unlockCellsLock(buyCellsPacks.uuid);
            return ResponseHandler.generateResponse(errors.NEED_TO_BE_2FA, HttpStatus.OK, null);
        }
        try {
            if(!TimeBasedOneTimePasswordUtil.validateCurrentNumber(currentUser.getSecretKey(), Integer.parseInt(buyCellsPacks.code), 0)) {
                fairLock.unlockUserLock(username);
                fairLock.unlockCellsLock(buyCellsPacks.uuid);
                return ResponseHandler.generateResponse(errors.INVALID_CODE, HttpStatus.OK, null);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            fairLock.unlockUserLock(username);
            fairLock.unlockCellsLock(buyCellsPacks.uuid);
            return ResponseHandler.generateResponse(errors.DEFAULT_ERROR, HttpStatus.OK, null);
        }
        User seller = userInterface.getUser(cellsPackToBuy.seller).get();
        if(currentUser.getBalance().compareTo(BigDecimal.valueOf(cellsPackToBuy.price)) < 0) {
            fairLock.unlockUserLock(username);
            fairLock.unlockCellsLock(buyCellsPacks.uuid);
            return ResponseHandler.generateResponse(errors.LOW_NATIVE_TOKENS_BALANCE, HttpStatus.OK, null);
        }
        cellsPackToBuy.setStatus(false);

        Optional<NFT> optionalNFT = nftInterface.findByIndex(new NFTSeekingRequest(buyCellsPacks.NFTId));
        if(optionalNFT.isEmpty()) {
            fairLock.unlockUserLock(username);
            fairLock.unlockCellsLock(buyCellsPacks.uuid);
            return ResponseHandler.generateResponse(errors.NFT_NOT_EXISTS, HttpStatus.OK, null);
        }
        unitService.sendTokens(new TransferTokensRequests(optionalNFT.get().name, "merchant", cellsPackToBuy.quantity, cellsPackToBuy.trapName.toLowerCase(Locale.ROOT)));
        currentUser.setBalance(currentUser.getBalance().subtract(BigDecimal.valueOf(cellsPackToBuy.price)));
        seller.setBalance(seller.getBalance().add(BigDecimal.valueOf(cellsPackToBuy.price)));

        userInterface.saveAll(List.of(seller, currentUser));
        marketInterface.save(cellsPackToBuy);
        return ResponseHandler.generateResponse(null, HttpStatus.OK, null);
    }

    public ResponseEntity<Object> withdrawCellsPacks(WithdrawCellsPacks withdrawCellsPacks, String username) {
        Optional<CellsLock> optionalCellsLock = fairLock.getCellsLock(withdrawCellsPacks.uuid);
        if(optionalCellsLock.isEmpty()) return ResponseHandler.generateResponse(errors.LOCK, HttpStatus.OK, null);
        Optional<UserLock> optionalUserLock = fairLock.getUserFairLock(username);
        if(optionalUserLock.isEmpty()) {
            fairLock.unlockCellsLock(withdrawCellsPacks.uuid);
            return ResponseHandler.generateResponse(errors.LOCK, HttpStatus.OK, null);
        }
        try {
            if(withdrawCellsPacks.checkNull()) {
                fairLock.unlockUserLock(username);
                fairLock.unlockCellsLock(withdrawCellsPacks.uuid);
                return ResponseHandler.generateResponse(errors.ALL_FIELDS_SHOULD_BE_FILLED_IN, HttpStatus.OK, null);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            fairLock.unlockUserLock(username);
            fairLock.unlockCellsLock(withdrawCellsPacks.uuid);
            return ResponseHandler.generateResponse(errors.DEFAULT_ERROR, HttpStatus.OK, null);
        }
        Optional<ConnectedWallet> optionalConnectedWallet = connectedWalletInterface.findByUsername(username);
        if(optionalConnectedWallet.isEmpty()) {
            fairLock.unlockUserLock(username);
            fairLock.unlockCellsLock(withdrawCellsPacks.uuid);
            return ResponseHandler.generateResponse(errors.METAMASK_ERROR, HttpStatus.OK, null);
        }
        Optional<CellsPack> optionalCellsPack = marketInterface.findAbstractByUuidAndStatus(new ProductSeekingRequest(withdrawCellsPacks.uuid, true));
        if(optionalCellsPack.isEmpty()) {
            fairLock.unlockUserLock(username);
            fairLock.unlockCellsLock(withdrawCellsPacks.uuid);
            return ResponseHandler.generateResponse(errors.CELLSPACK_NOT_FOUND, HttpStatus.OK, null);
        }
        CellsPack cellsPack = optionalCellsPack.get();
        Optional<NFT> optionalNFT = nftInterface.findByIndex(new NFTSeekingRequest(cellsPack.nftName));
        if(optionalNFT.isEmpty()) {
            fairLock.unlockUserLock(username);
            fairLock.unlockCellsLock(withdrawCellsPacks.uuid);
            return ResponseHandler.generateResponse(errors.NFT_NOT_EXISTS, HttpStatus.OK, null);
        }
        NFT nft = optionalNFT.get();
        if(balanceInterface.isOwnerOfNFT(username, nft.index, withdrawCellsPacks.chainName)) {
            fairLock.unlockUserLock(username);
            fairLock.unlockCellsLock(withdrawCellsPacks.uuid);
            return ResponseHandler.generateResponse(errors.OWNERSHIP_ERROR, HttpStatus.OK, null);
        }
        CellsPack cellsPackToBuy = optionalCellsPack.get();
        User user = userInterface.getUser(username).get();
        if(!user.isTwoFA()) {
            fairLock.unlockUserLock(username);
            fairLock.unlockCellsLock(withdrawCellsPacks.uuid);
            return ResponseHandler.generateResponse(errors.NEED_TO_BE_2FA, HttpStatus.OK, null);
        }
        try {
            if(!TimeBasedOneTimePasswordUtil.validateCurrentNumber(user.getSecretKey(), Integer.parseInt(withdrawCellsPacks.code), 0)) {
                fairLock.unlockUserLock(username);
                fairLock.unlockCellsLock(withdrawCellsPacks.uuid);
                return ResponseHandler.generateResponse(errors.INVALID_CODE, HttpStatus.OK, null);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            fairLock.unlockUserLock(username);
            fairLock.unlockCellsLock(withdrawCellsPacks.uuid);
            return ResponseHandler.generateResponse(errors.DEFAULT_ERROR, HttpStatus.OK, null);
        }
        cellsPackToBuy.setStatus(false);
        unitService.sendTokens(new TransferTokensRequests("merchant", nft.name, cellsPackToBuy.quantity, cellsPackToBuy.trapName.toLowerCase(Locale.ROOT)));
        marketInterface.save(cellsPackToBuy);
        fairLock.unlockUserLock(username);
        return ResponseHandler.generateResponse(null, HttpStatus.OK, null);
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
