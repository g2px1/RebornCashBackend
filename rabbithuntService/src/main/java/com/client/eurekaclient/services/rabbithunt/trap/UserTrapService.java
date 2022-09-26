package com.client.eurekaclient.services.rabbithunt.trap;

import com.client.eurekaclient.messages.ErrorMessage;
import com.client.eurekaclient.models.DTO.users.User;
import com.client.eurekaclient.models.lock.UserLock;
import com.client.eurekaclient.models.nft.NFT;
import com.client.eurekaclient.models.rabbithunt.trap.CellsTransactions;
import com.client.eurekaclient.models.rabbithunt.trap.Trap;
import com.client.eurekaclient.models.request.NFT.NFTSeekingRequest;
import com.client.eurekaclient.models.request.rabbithunt.trap.BuyCellsRequest;
import com.client.eurekaclient.models.request.unit.TransferTokensRequests;
import com.client.eurekaclient.models.request.web3.ConnectedWallet;
import com.client.eurekaclient.models.response.ResponseHandler;
import com.client.eurekaclient.models.response.trap.TrapResponse;
import com.client.eurekaclient.repositories.CellsTransactionRepository;
import com.client.eurekaclient.repositories.TokensRepository;
import com.client.eurekaclient.repositories.TrapRepository;
import com.client.eurekaclient.services.lock.FairLock;
import com.client.eurekaclient.services.openfeign.transactions.unit.UnitInterface;
import com.client.eurekaclient.services.openfeign.wallets.BalanceInterface;
import com.client.eurekaclient.services.openfeign.NFT.NFTInterface;
import com.client.eurekaclient.services.openfeign.users.UserInterface;
import com.client.eurekaclient.services.openfeign.wallets.ConnectedWalletInterface;
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

import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class UserTrapService {
    @Autowired
    private TokensRepository tokensRepository;
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
    private UnitInterface unitInterface;
    @Autowired
    private FairLock fairLock;
    private static final Logger logger = LoggerFactory.getLogger(UserTrapService.class);

    public ResponseEntity<Object> loadTraps(HashMap<String, Integer> pageNumber) {
        Pageable paging = PageRequest.of(pageNumber.get("pageNumber"), 5);
        Page<Trap> page = trapRepository.findAllByStatus("active", paging);
        return ResponseHandler.generateResponse(null, HttpStatus.OK, Map.of("content", page.getContent().stream().map(TrapResponse::build).collect(Collectors.toList()), "currentPage", page.getNumber(), "totalItems", page.getTotalElements(), "totalPages", page.getTotalPages()));
    }

    public ResponseEntity<Object> buyCells(BuyCellsRequest buyCellsRequest, String username) {
        Optional<UserLock> userLock = fairLock.getFairLock(username);
        if (userLock.isEmpty()) return ResponseHandler.generateResponse(ErrorMessage.LOCK, HttpStatus.OK, null);
        if (!trapRepository.existsByName(buyCellsRequest.trapName)) {
            fairLock.unlock(username);
            return ResponseHandler.generateResponse(ErrorMessage.TRAP_NOT_EXIST, HttpStatus.OK, null);
        }
        Optional<User> optionalUser = userInterface.getUser(username);
        if (optionalUser.isEmpty()) {
            fairLock.unlock(username);
            return ResponseHandler.generateResponse(ErrorMessage.USER_NOT_FOUND, HttpStatus.OK, null);
        }
        User user = optionalUser.get();
        try {
            if (!TimeBasedOneTimePasswordUtil.validateCurrentNumber(user.getSecretKey(), Integer.parseInt(buyCellsRequest.code), 0)) {
                fairLock.unlock(username);
                return ResponseHandler.generateResponse(ErrorMessage.INVALID_CODE, HttpStatus.OK, null);
            }
        } catch (GeneralSecurityException e) {
            fairLock.unlock(username);
            logger.error(e.getMessage());
            return ResponseHandler.generateResponse(ErrorMessage.DEFAULT_ERROR, HttpStatus.OK, null);
        }
        Trap trap = trapRepository.findByName(buyCellsRequest.trapName);
        if (!trap.status) {
            fairLock.unlock(username);
            return ResponseHandler.generateResponse(ErrorMessage.TRAP_EXPIRED, HttpStatus.OK, null);
        }
        Optional<ConnectedWallet> optionalConnectedWallet = connectedWalletInterface.findByUsername(username);
        if (optionalConnectedWallet.isEmpty()) {
            fairLock.unlock(username);
            return ResponseHandler.generateResponse(ErrorMessage.METAMASK_ERROR, HttpStatus.OK, null);
        }
        Optional<NFT> optionalNFT = nftInterface.findByIndex(new NFTSeekingRequest(buyCellsRequest.nftIndex));
        if (optionalNFT.isEmpty()) {
            fairLock.unlock(username);
            return ResponseHandler.generateResponse(ErrorMessage.NFT_NOT_EXISTS, HttpStatus.OK, null);
        }
        NFT nft = optionalNFT.get();
        if (!nftInterface.isOwnerOfNFT(username, buyCellsRequest.nftIndex, buyCellsRequest.chainName)) {
            fairLock.unlock(username);
            return ResponseHandler.generateResponse(ErrorMessage.OWNERSHIP_ERROR, HttpStatus.OK, null);
        }
        if (buyCellsRequest.quantity == 0) {
            fairLock.unlock(username);
            return ResponseHandler.generateResponse(String.format(ErrorMessage.INVALID_DATA, "quantity = 0"), HttpStatus.OK, null);
        }
        Optional<JSONObject> optionalJsonBalance = balanceInterface.getBalance(nft.name);
        if (optionalJsonBalance.isEmpty()) {
            fairLock.unlock(username);
            return ResponseHandler.generateResponse(ErrorMessage.DEFAULT_ERROR, HttpStatus.OK, null);
        }
        Optional<Double> optionalInCarrotsBalance = UserTrapService.getValueInDouble(optionalJsonBalance.get(), "carrot");
        if (optionalInCarrotsBalance.isEmpty()) {
            fairLock.unlock(username);
            return ResponseHandler.generateResponse(ErrorMessage.DEFAULT_ERROR, HttpStatus.OK, null);
        }
        double inCarrotsBalance = optionalInCarrotsBalance.get();

        if (buyCellsRequest.quantity > trap.cellsAvailable) {
            fairLock.unlock(username);
            return ResponseHandler.generateResponse(ErrorMessage.TRAP_OUT_OF_EMISSION, HttpStatus.OK, null);
        }
        Optional<JSONObject> optionalPrice = YahooFinanceRequest.getOptionPrice(trap.optionName);
        if (optionalPrice.isEmpty()) {
            fairLock.unlock(username);
            return ResponseHandler.generateResponse(ErrorMessage.TRAP_NOT_EXIST, HttpStatus.OK, null);
        }
        Double optionPrice;
        try {
            optionPrice = YahooFinanceRequest.getOptionOptionalRegularMarketPrice(trap.optionName);
        } catch (Exception e) {
            fairLock.unlock(username);
            return ResponseHandler.generateResponse(ErrorMessage.DEFAULT_ERROR, HttpStatus.OK, null);
        }
        if (buyCellsRequest.quantity > inCarrotsBalance * 100) {
            fairLock.unlock(username);
            return ResponseHandler.generateResponse(ErrorMessage.NOT_ENOUGH_CARROTS, HttpStatus.OK, null);
        }
        trap.setCellsAvailable(trap.cellsAvailable - buyCellsRequest.quantity);
        trap.setTokenPerCell((optionPrice / trap.cells));
        trapRepository.save(trap);

        JSONObject outJSON = unitInterface.sendTokens(new TransferTokensRequests("merchant", nft.name, buyCellsRequest.quantity * 100, "carrot"));
        JSONObject inJSON = unitInterface.sendTokens(new TransferTokensRequests(nft.name, "merchant", buyCellsRequest.quantity * 100, trap.name.toLowerCase(Locale.ROOT)));
        cellsTransactionRepository.save(new CellsTransactions(trap, nft.name, buyCellsRequest.quantity, outJSON.getString("hash"), inJSON.getString("hash")));
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
