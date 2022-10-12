package com.client.eurekaclient.services.rabbithunt.mine;

import com.client.eurekaclient.messages.ErrorMessage;
import com.client.eurekaclient.models.DTO.users.User;
import com.client.eurekaclient.models.lock.UserLock;
import com.client.eurekaclient.models.nft.NFT;
import com.client.eurekaclient.models.goldenrush.mine.CellsTransactions;
import com.client.eurekaclient.models.goldenrush.mine.Mine;
import com.client.eurekaclient.models.request.NFT.NFTSeekingRequest;
import com.client.eurekaclient.models.request.goldenrush.mine.BuyCellsRequest;
import com.client.eurekaclient.models.request.unit.TransferTokensRequests;
import com.client.eurekaclient.models.request.web3.ConnectedWallet;
import com.client.eurekaclient.models.response.ResponseHandler;
import com.client.eurekaclient.models.response.mine.TrapResponse;
import com.client.eurekaclient.repositories.CellsTransactionRepository;
import com.client.eurekaclient.repositories.ScheduledTransactionRepository;
import com.client.eurekaclient.repositories.MineRepository;
import com.client.eurekaclient.services.lock.FairLock;
import com.client.eurekaclient.services.openfeign.transactions.unit.UnitInterface;
import com.client.eurekaclient.services.openfeign.wallets.BalanceInterface;
import com.client.eurekaclient.services.openfeign.NFT.NFTInterface;
import com.client.eurekaclient.services.openfeign.users.UserInterface;
import com.client.eurekaclient.services.openfeign.wallets.ConnectedWalletInterface;
import com.client.eurekaclient.services.rabbithunt.transaction.ScheduledTxService;
import com.client.eurekaclient.services.unit.UnitService;
import com.client.eurekaclient.utilities.http.finance.YahooFinanceRequest;
import com.j256.twofactorauth.TimeBasedOneTimePasswordUtil;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class UserMineService {
    @Autowired
    private MineRepository mineRepository;
    @Autowired
    private UserInterface userInterface;
    @Autowired
    private NFTInterface nftInterface;
    @Autowired
    private BalanceInterface balanceInterface;
    @Autowired
    private ConnectedWalletInterface connectedWalletInterface;
    @Autowired
    private CellsTransactionRepository cellsTransactionRepository;
    @Autowired
    private ScheduledTransactionRepository scheduledTransactionRepository;
    @Autowired
    private UnitService unitService;
    @Autowired
    private ScheduledTxService scheduledTxService;
    @Autowired
    private FairLock fairLock;
    private static final Logger logger = LoggerFactory.getLogger(UserMineService.class);

    public ResponseEntity<Object> loadMines(HashMap<String, Integer> pageNumber) {
        if (!pageNumber.containsKey("page")) return ResponseHandler.generateResponse(ErrorMessage.INVALID_DATA, HttpStatus.OK, null);
        Pageable paging = PageRequest.of(pageNumber.get("page"), 5);
        Page<Mine> page = mineRepository.findAllByStatus(true, paging);
        return ResponseHandler.generateResponse(null, HttpStatus.OK, Map.of("content", page.getContent().stream().map(TrapResponse::build).collect(Collectors.toList()), "currentPage", page.getNumber(), "totalItems", page.getTotalElements(), "totalPages", page.getTotalPages()));
    }

    public ResponseEntity<Object> getMinesImage(String trapImage) {
        ByteArrayResource inputStream;
        try {
            inputStream = new ByteArrayResource(Files.readAllBytes(Paths.get(String.format("./images/traps/%s", trapImage))));
        } catch (Exception e) {
            return ResponseHandler.generateResponse("Image not exists.", HttpStatus.OK, false);
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentLength(inputStream.contentLength())
                .body(inputStream);
    }

    public ResponseEntity<Object> buyCells(BuyCellsRequest buyCellsRequest, String username) {
        Optional<UserLock> userLock = fairLock.getUserFairLock(username);
        if (userLock.isEmpty()) return ResponseHandler.generateResponse(ErrorMessage.LOCK, HttpStatus.BAD_REQUEST, null);
        if (!mineRepository.existsByName(buyCellsRequest.trapName)) {
            fairLock.unlockUserLock(username);
            return ResponseHandler.generateResponse(ErrorMessage.TRAP_NOT_EXIST, HttpStatus.BAD_REQUEST, null);
        }
        Optional<User> optionalUser = userInterface.getUser(username);
        if (optionalUser.isEmpty()) {
            fairLock.unlockUserLock(username);
            return ResponseHandler.generateResponse(ErrorMessage.USER_NOT_FOUND, HttpStatus.BAD_REQUEST, null);
        }
        User user = optionalUser.get();
        try {
            if (!TimeBasedOneTimePasswordUtil.validateCurrentNumber(user.getSecretKey(), Integer.parseInt(buyCellsRequest.code), 0)) {
                fairLock.unlockUserLock(username);
                return ResponseHandler.generateResponse(ErrorMessage.INVALID_CODE, HttpStatus.BAD_REQUEST, null);
            }
        } catch (GeneralSecurityException e) {
            fairLock.unlockUserLock(username);
            logger.error(e.getMessage());
            return ResponseHandler.generateResponse(ErrorMessage.DEFAULT_ERROR, HttpStatus.BAD_REQUEST, null);
        }
        Mine mine = mineRepository.findByName(buyCellsRequest.trapName);
        if (!mine.status) {
            fairLock.unlockUserLock(username);
            return ResponseHandler.generateResponse(ErrorMessage.TRAP_EXPIRED, HttpStatus.BAD_REQUEST, null);
        }
        Optional<ConnectedWallet> optionalConnectedWallet = connectedWalletInterface.findByUsername(username);
        if (optionalConnectedWallet.isEmpty()) {
            fairLock.unlockUserLock(username);
            return ResponseHandler.generateResponse(ErrorMessage.METAMASK_ERROR, HttpStatus.BAD_REQUEST, null);
        }
        Optional<NFT> optionalNFT = nftInterface.findByIndex(new NFTSeekingRequest(buyCellsRequest.nftIndex));
        if (optionalNFT.isEmpty()) {
            fairLock.unlockUserLock(username);
            return ResponseHandler.generateResponse(ErrorMessage.NFT_NOT_EXISTS, HttpStatus.BAD_REQUEST, null);
        }
        NFT nft = optionalNFT.get();
        if (!balanceInterface.isOwnerOfNFT(username, buyCellsRequest.nftIndex, buyCellsRequest.chainName)) {
            fairLock.unlockUserLock(username);
            return ResponseHandler.generateResponse(ErrorMessage.OWNERSHIP_ERROR, HttpStatus.BAD_REQUEST, null);
        }
        if (buyCellsRequest.quantity == 0) {
            fairLock.unlockUserLock(username);
            return ResponseHandler.generateResponse(String.format(ErrorMessage.INVALID_DATA, "quantity = 0"), HttpStatus.OK, null);
        }
        Optional<JSONObject> optionalJsonBalance = Optional.ofNullable(unitService.getBalance(nft.name));
        if (optionalJsonBalance.isEmpty() || optionalJsonBalance.get().isEmpty()) {
            fairLock.unlockUserLock(username);
            return ResponseHandler.generateResponse(ErrorMessage.DEFAULT_ERROR, HttpStatus.BAD_REQUEST, null);
        }
        Optional<Double> optionalInCarrotsBalance = UserMineService.getValueInDouble(optionalJsonBalance.get(), "silver_coin");
        if (optionalInCarrotsBalance.isEmpty()) {
            fairLock.unlockUserLock(username);
            return ResponseHandler.generateResponse(ErrorMessage.DEFAULT_ERROR, HttpStatus.BAD_REQUEST, null);
        }
        double inCarrotsBalance = optionalInCarrotsBalance.get();

        if (buyCellsRequest.quantity > mine.cellsAvailable) {
            fairLock.unlockUserLock(username);
            return ResponseHandler.generateResponse(ErrorMessage.TRAP_OUT_OF_EMISSION, HttpStatus.BAD_REQUEST, null);
        }
        Optional<JSONObject> optionalPrice = YahooFinanceRequest.getOptionPrice(mine.optionName);
        if (optionalPrice.isEmpty()) {
            fairLock.unlockUserLock(username);
            return ResponseHandler.generateResponse(ErrorMessage.TRAP_NOT_EXIST, HttpStatus.BAD_REQUEST, null);
        }
        Double optionPrice;
        try {
            optionPrice = YahooFinanceRequest.getOptionOptionalRegularMarketPrice(mine.optionName);
        } catch (Exception e) {
            fairLock.unlockUserLock(username);
            return ResponseHandler.generateResponse(ErrorMessage.DEFAULT_ERROR, HttpStatus.BAD_REQUEST, null);
        }
        if (buyCellsRequest.quantity > inCarrotsBalance * 100) {
            fairLock.unlockUserLock(username);
            return ResponseHandler.generateResponse(ErrorMessage.NOT_ENOUGH_CARROTS, HttpStatus.BAD_REQUEST, null);
        }
        mine.setCellsAvailable(mine.cellsAvailable - buyCellsRequest.quantity);
        mine.setTokenPerCell((optionPrice / mine.cells));
        mineRepository.save(mine);

        JSONObject outJSON = unitService.sendTokens(new TransferTokensRequests("merchant", nft.name, buyCellsRequest.quantity * 100, "silver_coin"));
        JSONObject inJSON = unitService.sendTokens(new TransferTokensRequests(nft.name, "merchant", buyCellsRequest.quantity, mine.name.toLowerCase(Locale.ROOT)));
        cellsTransactionRepository.save(new CellsTransactions(mine, nft.name, buyCellsRequest.quantity, outJSON.get("hash").toString(), inJSON.get("hash").toString()));
        fairLock.unlockUserLock(username);
        return ResponseHandler.generateResponse("Ok", HttpStatus.OK, Map.of("outTx", outJSON.get("hash"), "inTx", inJSON.get("hash")));
    }

    public static Optional<Double> getValueInDouble(JSONObject balance, String tokenName) {
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
