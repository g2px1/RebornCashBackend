package com.client.eurekaclient.services.rabbithunt.trap;

import com.client.eurekaclient.messages.ErrorMessage;
import com.client.eurekaclient.models.DTO.users.User;
import com.client.eurekaclient.models.lock.UserLock;
import com.client.eurekaclient.models.nft.NFT;
import com.client.eurekaclient.models.rabbithunt.trap.CellsTransactions;
import com.client.eurekaclient.models.rabbithunt.trap.Trap;
import com.client.eurekaclient.models.request.NFT.NFTSeekingRequest;
import com.client.eurekaclient.models.request.rabbithunt.token.InvestmentInBurgerRequest;
import com.client.eurekaclient.models.request.rabbithunt.trap.BuyCellsRequest;
import com.client.eurekaclient.models.request.unit.TransferTokensRequests;
import com.client.eurekaclient.models.request.web3.ConnectedWallet;
import com.client.eurekaclient.models.response.ResponseHandler;
import com.client.eurekaclient.models.response.trap.TrapResponse;
import com.client.eurekaclient.models.scheduled.transactions.ScheduledTransaction;
import com.client.eurekaclient.repositories.CellsTransactionRepository;
import com.client.eurekaclient.repositories.ScheduledTransactionRepository;
import com.client.eurekaclient.repositories.TrapRepository;
import com.client.eurekaclient.services.lock.FairLock;
import com.client.eurekaclient.services.openfeign.transactions.unit.UnitInterface;
import com.client.eurekaclient.services.openfeign.wallets.BalanceInterface;
import com.client.eurekaclient.services.openfeign.NFT.NFTInterface;
import com.client.eurekaclient.services.openfeign.users.UserInterface;
import com.client.eurekaclient.services.openfeign.wallets.ConnectedWalletInterface;
import com.client.eurekaclient.services.rabbithunt.transaction.ScheduledTxService;
import com.client.eurekaclient.utilities.http.finance.YahooFinanceRequest;
import com.j256.twofactorauth.TimeBasedOneTimePasswordUtil;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.GeneralSecurityException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class UserTrapService {
    @Autowired
    private TrapRepository trapRepository;
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
    private UnitInterface unitInterface;
    @Autowired
    private ScheduledTxService scheduledTxService;
    @Autowired
    private FairLock fairLock;
    private static final Logger logger = LoggerFactory.getLogger(UserTrapService.class);

    public ResponseEntity<Object> loadTraps(HashMap<String, Integer> pageNumber) {
        if (!pageNumber.containsKey("page")) return ResponseHandler.generateResponse(ErrorMessage.INVALID_DATA, HttpStatus.OK, null);
        Pageable paging = PageRequest.of(pageNumber.get("pageNumber"), 5);
        Page<Trap> page = trapRepository.findAllByStatus("active", paging);
        return ResponseHandler.generateResponse(null, HttpStatus.OK, Map.of("content", page.getContent().stream().map(TrapResponse::build).collect(Collectors.toList()), "currentPage", page.getNumber(), "totalItems", page.getTotalElements(), "totalPages", page.getTotalPages()));
    }

    public ResponseEntity<Object> buyCells(BuyCellsRequest buyCellsRequest, String username) {
        Optional<UserLock> userLock = fairLock.getUserFairLock(username);
        if (userLock.isEmpty()) return ResponseHandler.generateResponse(ErrorMessage.LOCK, HttpStatus.OK, null);
        if (!trapRepository.existsByName(buyCellsRequest.trapName)) {
            fairLock.unlockUserLock(username);
            return ResponseHandler.generateResponse(ErrorMessage.TRAP_NOT_EXIST, HttpStatus.OK, null);
        }
        Optional<User> optionalUser = userInterface.getUser(username);
        if (optionalUser.isEmpty()) {
            fairLock.unlockUserLock(username);
            return ResponseHandler.generateResponse(ErrorMessage.USER_NOT_FOUND, HttpStatus.OK, null);
        }
        User user = optionalUser.get();
        try {
            if (!TimeBasedOneTimePasswordUtil.validateCurrentNumber(user.getSecretKey(), Integer.parseInt(buyCellsRequest.code), 0)) {
                fairLock.unlockUserLock(username);
                return ResponseHandler.generateResponse(ErrorMessage.INVALID_CODE, HttpStatus.OK, null);
            }
        } catch (GeneralSecurityException e) {
            fairLock.unlockUserLock(username);
            logger.error(e.getMessage());
            return ResponseHandler.generateResponse(ErrorMessage.DEFAULT_ERROR, HttpStatus.OK, null);
        }
        Trap trap = trapRepository.findByName(buyCellsRequest.trapName);
        if (!trap.status) {
            fairLock.unlockUserLock(username);
            return ResponseHandler.generateResponse(ErrorMessage.TRAP_EXPIRED, HttpStatus.OK, null);
        }
        Optional<ConnectedWallet> optionalConnectedWallet = connectedWalletInterface.findByUsername(username);
        if (optionalConnectedWallet.isEmpty()) {
            fairLock.unlockUserLock(username);
            return ResponseHandler.generateResponse(ErrorMessage.METAMASK_ERROR, HttpStatus.OK, null);
        }
        Optional<NFT> optionalNFT = nftInterface.findByIndex(new NFTSeekingRequest(buyCellsRequest.nftIndex));
        if (optionalNFT.isEmpty()) {
            fairLock.unlockUserLock(username);
            return ResponseHandler.generateResponse(ErrorMessage.NFT_NOT_EXISTS, HttpStatus.OK, null);
        }
        NFT nft = optionalNFT.get();
        if (!balanceInterface.isOwnerOfNFT(username, buyCellsRequest.nftIndex, buyCellsRequest.chainName)) {
            fairLock.unlockUserLock(username);
            return ResponseHandler.generateResponse(ErrorMessage.OWNERSHIP_ERROR, HttpStatus.OK, null);
        }
        if (buyCellsRequest.quantity == 0) {
            fairLock.unlockUserLock(username);
            return ResponseHandler.generateResponse(String.format(ErrorMessage.INVALID_DATA, "quantity = 0"), HttpStatus.OK, null);
        }
        Optional<JSONObject> optionalJsonBalance = balanceInterface.getBalance(nft.name);
        if (optionalJsonBalance.isEmpty()) {
            fairLock.unlockUserLock(username);
            return ResponseHandler.generateResponse(ErrorMessage.DEFAULT_ERROR, HttpStatus.OK, null);
        }
        Optional<Double> optionalInCarrotsBalance = UserTrapService.getValueInDouble(optionalJsonBalance.get(), "carrot");
        if (optionalInCarrotsBalance.isEmpty()) {
            fairLock.unlockUserLock(username);
            return ResponseHandler.generateResponse(ErrorMessage.DEFAULT_ERROR, HttpStatus.OK, null);
        }
        double inCarrotsBalance = optionalInCarrotsBalance.get();

        if (buyCellsRequest.quantity > trap.cellsAvailable) {
            fairLock.unlockUserLock(username);
            return ResponseHandler.generateResponse(ErrorMessage.TRAP_OUT_OF_EMISSION, HttpStatus.OK, null);
        }
        Optional<JSONObject> optionalPrice = YahooFinanceRequest.getOptionPrice(trap.optionName);
        if (optionalPrice.isEmpty()) {
            fairLock.unlockUserLock(username);
            return ResponseHandler.generateResponse(ErrorMessage.TRAP_NOT_EXIST, HttpStatus.OK, null);
        }
        Double optionPrice;
        try {
            optionPrice = YahooFinanceRequest.getOptionOptionalRegularMarketPrice(trap.optionName);
        } catch (Exception e) {
            fairLock.unlockUserLock(username);
            return ResponseHandler.generateResponse(ErrorMessage.DEFAULT_ERROR, HttpStatus.OK, null);
        }
        if (buyCellsRequest.quantity > inCarrotsBalance * 100) {
            fairLock.unlockUserLock(username);
            return ResponseHandler.generateResponse(ErrorMessage.NOT_ENOUGH_CARROTS, HttpStatus.OK, null);
        }
        trap.setCellsAvailable(trap.cellsAvailable - buyCellsRequest.quantity);
        trap.setTokenPerCell((optionPrice / trap.cells));
        trapRepository.save(trap);

        JSONObject outJSON = unitInterface.sendTokens(new TransferTokensRequests("merchant", nft.name, buyCellsRequest.quantity * 100, "carrot"));
        JSONObject inJSON = unitInterface.sendTokens(new TransferTokensRequests(nft.name, "merchant", buyCellsRequest.quantity * 100, trap.name.toLowerCase(Locale.ROOT)));
        cellsTransactionRepository.save(new CellsTransactions(trap, nft.name, buyCellsRequest.quantity, outJSON.getString("hash"), inJSON.getString("hash")));
        fairLock.unlockUserLock(username);
        return ResponseHandler.generateResponse("Ok", HttpStatus.OK, Map.of("outTx", outJSON.get("hash"), "inTx", inJSON.get("hash")));
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
            return ResponseHandler.generateResponse(ErrorMessage.LOW_MEAT_BALANCE, HttpStatus.BAD_REQUEST, null);
        }
        NFT nft = optionalNFT.get();
        Optional<List<ScheduledTransaction>> optionalScheduledTransactionList = scheduledTransactionRepository.findByNftNameAndActiveTillLessThanAndReverted(nft.name, new Date().getTime(), false);
        optionalScheduledTransactionList.ifPresent(layer1ExpiringTransactionsList -> { // check if list !empty
            layer1ExpiringTransactionsList.forEach(scheduledTransaction -> { // looking for all Transactions and their amounts
                unitInterface.sendTokens(new TransferTokensRequests("merchant", nft.name, scheduledTransaction.amount, "meat"));
                scheduledTransaction.setReverted(true); // setting status of transaction
            });
            scheduledTransactionRepository.saveAll(optionalScheduledTransactionList.get()); // saving changes if exists
        });

        Optional<JSONObject> optionalJsonBalance = balanceInterface.getBalance(nft.name);
        if (optionalJsonBalance.isEmpty()) {
            fairLock.unlockUserLock(username);
            return ResponseHandler.generateResponse(ErrorMessage.DEFAULT_ERROR, HttpStatus.OK, null);
        }
        Optional<Double> optionalInCarrotsBalance = UserTrapService.getValueInDouble(optionalJsonBalance.get(), "meat");
        if (optionalInCarrotsBalance.isEmpty()) {
            fairLock.unlockUserLock(username);
            return ResponseHandler.generateResponse(ErrorMessage.DEFAULT_ERROR, HttpStatus.OK, null);
        }
        double inMeatBalance = optionalInCarrotsBalance.get();
        if (inMeatBalance < investmentInBurgerRequest.quantityOfBurgers * 10) {
            fairLock.unlockUserLock(username);
            return ResponseHandler.generateResponse(ErrorMessage.LOW_MEAT_BALANCE, HttpStatus.OK, null);
        }

        JSONObject outMeat = unitInterface.sendTokens(new TransferTokensRequests("merchant", nft.name, investmentInBurgerRequest.quantityOfBurgers * 10, "meat"));
        JSONObject inBurger = unitInterface.sendTokens(new TransferTokensRequests(nft.name, "merchant", investmentInBurgerRequest.quantityOfBurgers, "burger"));
        user.setBalance(user.getBalance().subtract(BigDecimal.valueOf(10)));
        userInterface.saveUser(user);
        scheduledTxService.subtractTxByNft(nft.name, investmentInBurgerRequest.quantityOfBurgers * 10);
        fairLock.unlockUserLock(username);
        return ResponseHandler.generateResponse("", HttpStatus.OK, new HashMap<>(Map.of("meatTransactionHash", outMeat.toMap(), "burgerTransactionHash", inBurger.toMap())));
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
