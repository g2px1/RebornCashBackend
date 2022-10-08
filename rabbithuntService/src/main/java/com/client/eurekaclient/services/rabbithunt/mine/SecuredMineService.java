package com.client.eurekaclient.services.rabbithunt.mine;

import com.client.eurekaclient.constants.Constants;
import com.client.eurekaclient.messages.ErrorMessage;
import com.client.eurekaclient.models.goldenrush.cells.CellsPack;
import com.client.eurekaclient.models.goldenrush.mine.Mine;
import com.client.eurekaclient.models.goldenrush.tokens.Token;
import com.client.eurekaclient.models.goldenrush.mine.CellsTransactions;
import com.client.eurekaclient.models.request.goldenrush.mine.CloseMineRequest;
import com.client.eurekaclient.models.request.goldenrush.mine.MineRequest;
import com.client.eurekaclient.models.request.unit.TransferTokensRequests;
import com.client.eurekaclient.models.response.ResponseHandler;
import com.client.eurekaclient.models.scheduled.transactions.ScheduledTransaction;
import com.client.eurekaclient.repositories.*;
import com.client.eurekaclient.services.openfeign.transactions.unit.UnitInterface;
import com.client.eurekaclient.utilities.upload.StorageService;
import com.client.eurekaclient.utilities.http.finance.YahooFinanceRequest;
import com.client.eurekaclient.utilities.http.PostRequest;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class SecuredMineService {
    @Autowired
    private TokensRepository tokensRepository;
    @Autowired
    private CellsTransactionRepository cellsTransactionRepository;
    @Autowired
    private ScheduledTransactionRepository scheduledTransactionRepository;
    @Autowired
    private CellsPackRepository cellsPackRepository;
    @Autowired
    private UnitInterface unitInterface;
    @Autowired
    private MineRepository mineRepository;
    @Value("${file.upload-dir}")
    private String directory;
    private final StorageService storageService;
    private static final Logger logger = LoggerFactory.getLogger(SecuredMineService.class);

    @Autowired
    public SecuredMineService(StorageService storageService) {
        this.storageService = storageService;
    }

    private static String convertToHex(String tokenStructure) {
        return tokenStructure.chars().mapToObj(Integer::toHexString).collect(Collectors.joining());
    }

    public ResponseEntity<Object> createMine(MineRequest mineRequest) {
        if (!tokensRepository.existsByName(mineRequest.tokenName.trim()))
            return ResponseHandler.generateResponse("Token doesn't exist in UNIT.", HttpStatus.BAD_REQUEST, null);
        try {
            if (mineRequest.checkNull())
                return ResponseHandler.generateResponse(ErrorMessage.ALL_FIELDS_SHOULD_BE_FILLED_IN, HttpStatus.BAD_REQUEST, null);
        } catch (IllegalAccessException e) {
            logger.error(e.getMessage());
            return ResponseHandler.generateResponse(ErrorMessage.DEFAULT_ERROR, HttpStatus.BAD_REQUEST, null);
        }
        if (mineRepository.existsByName(StringUtils.capitalize(mineRequest.name.toLowerCase(Locale.ROOT)).trim()))
            return ResponseHandler.generateResponse("Mine exists.", HttpStatus.BAD_REQUEST, null);
        if (!YahooFinanceRequest.optionExists(mineRequest.optionName.toUpperCase(Locale.ROOT).trim()))
            return ResponseHandler.generateResponse("Option doesn't exist on Yahoo finance.", HttpStatus.BAD_REQUEST, null);
        if (new File(directory, mineRequest.imageFile.getOriginalFilename()).exists())
            return ResponseHandler.generateResponse("File exists", HttpStatus.BAD_REQUEST, null);
        if (mineRequest.activeTill == -1)
            return ResponseHandler.generateResponse("'activeTill' should be filled in", HttpStatus.BAD_REQUEST, null);
        storageService.store(mineRequest.imageFile);
        double optionPrice = YahooFinanceRequest.getOptionOptionalRegularMarketPrice(mineRequest.optionName) * 100;
        mineRepository.save(new Mine(StringUtils.capitalize(mineRequest.name.toLowerCase(Locale.ROOT)).trim(), UUID.randomUUID().toString(), mineRequest.imageFile.getOriginalFilename().trim(), mineRequest.cells, mineRequest.created, mineRequest.activeAfter, mineRequest.optionType.trim(), mineRequest.tools.trim(), Math.round(mineRequest.strikePrice), mineRequest.cells, mineRequest.tokenName.trim(), mineRequest.tokenTotalWeight, ((optionPrice * mineRequest.quantityOfLots) / mineRequest.cells), mineRequest.optionName.trim(), true, mineRequest.activeTill, mineRequest.quantityOfLots));
        PostRequest.createTokenTransaction(convertToHex(String.format("{\"name\": \"%s\", \"supply\": %s}", mineRequest.name.toLowerCase(Locale.ROOT).trim(), mineRequest.cells)));
        tokensRepository.save(new Token(mineRequest.name.toLowerCase(Locale.ROOT).trim(), BigDecimal.valueOf(mineRequest.cells), (short) 1, "mine"));
        return ResponseHandler.generateResponse("ok", HttpStatus.OK, true);
    }

    public ResponseEntity<Object> editMine(MineRequest mineRequest) {
        if (!mineRepository.existsByName(StringUtils.capitalize(mineRequest.name.toLowerCase(Locale.ROOT))))
            return ResponseHandler.generateResponse("Mine not exists.", HttpStatus.BAD_REQUEST, null);
        Mine mine = mineRepository.findByName(StringUtils.capitalize(mineRequest.name.toLowerCase(Locale.ROOT)));
        if (mineRequest.cells != 0 && mineRequest.cells < mine.cells)
            return ResponseHandler.generateResponse("Cells quantity can't be changed to lower one", HttpStatus.BAD_REQUEST, null);
        else {
            mine.setCellsAvailable(mineRequest.cells - mine.cells + mine.cellsAvailable);
            mine.setCells(mineRequest.cells);
        }
        if (!mine.name.equalsIgnoreCase(mineRequest.name))
            return ResponseHandler.generateResponse("Mine name can't be changed", HttpStatus.BAD_REQUEST, null);
        if (!mine.optionName.equalsIgnoreCase(mineRequest.optionName))
            return ResponseHandler.generateResponse("Option name can't be changed", HttpStatus.BAD_REQUEST, null);
        mine.updateFromRequest(mineRequest);
        mineRepository.save(mine);
        return ResponseHandler.generateResponse(null, HttpStatus.OK, true);
    }

    public ResponseEntity<Object> loadMine(HashMap<String, String> findMine) {
        return ResponseHandler.generateResponse(null, HttpStatus.OK, mineRepository.findByName(findMine.get("mineName")));
    }

    public ResponseEntity<Object> loadMinesImages() {
        return ResponseHandler.generateResponse(null, HttpStatus.OK, Stream.of(Objects.requireNonNull(new File(directory).listFiles()))
                .filter(file -> !file.isDirectory())
                .map(File::getName)
                .collect(Collectors.toSet()));
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

    public ResponseEntity<Object> loadMines(HashMap<String, Integer> pageNumber) {
        Pageable paging = PageRequest.of(pageNumber.get("pageNumber"), 5);
        Page<Mine> page = mineRepository.findAllByStatus(true, paging);
        return ResponseHandler.generateResponse(null, HttpStatus.OK, Map.of("content", page.getContent(), "currentPage", page.getNumber(), "totalItems", page.getTotalElements(), "totalPages", page.getTotalPages()));
    }

    public ResponseEntity<Object> closeMine(CloseMineRequest closeMineRequest) {
        if (!mineRepository.existsByName(closeMineRequest.mineName))
            return ResponseHandler.generateResponse(ErrorMessage.TRAP_NOT_EXIST, HttpStatus.OK, false);
        Mine mine = mineRepository.findByName(closeMineRequest.mineName);
        if (!mine.status) return ResponseHandler.generateResponse(ErrorMessage.TRAP_CLOSED, HttpStatus.OK, false);
        try {
            if (closeMineRequest.price != null) mine.setTokenPerCell(Double.parseDouble(closeMineRequest.price) / mine.cells);
            else mine.setTokenPerCell(YahooFinanceRequest.getOptionOptionalRegularMarketPrice(mine.optionName) * 100 / mine.cells);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        Optional<List<CellsTransactions>> optionalTrapsCellsTransactionsList = cellsTransactionRepository.findByTrap(mine);
        if (optionalTrapsCellsTransactionsList.isEmpty()) {
            mine.setStatus(false);
            mineRepository.save(mine);
            return ResponseHandler.generateResponse(ErrorMessage.TRAP_IS_FULL, HttpStatus.OK, null);
        }

        Runnable firstHalf = (() -> {
            List<ScheduledTransaction> scheduledTransactionArrayList = new ArrayList<>();
            List<CellsTransactions> cellsTransactions = optionalTrapsCellsTransactionsList.get();
            for (int i = 0; i < cellsTransactions.size(); i++) {
                double amountToTransfer = mine.tokenPerCell * cellsTransactions.get(i).quantity;
                JSONObject tokenDistribution = unitInterface.sendTokens(new TransferTokensRequests(cellsTransactions.get(i).nftName, "merchant", amountToTransfer, mine.tokenName));
                scheduledTransactionArrayList.add(new ScheduledTransaction(Instant.now().plusMillis(Constants.FIVE_DAYS_MS).toEpochMilli(), tokenDistribution.getString("hash"), cellsTransactions.get(i).nftName, amountToTransfer, mine.tokenName, false));
                try {
                    TimeUnit.SECONDS.sleep(3);
                } catch (Exception e) {
                    logger.error(e.getMessage());
                }
            }
            scheduledTransactionRepository.saveAll(scheduledTransactionArrayList);
            List<CellsPack> cellsPackList = new ArrayList<>();
            cellsPackRepository.findByTrap(mine).forEach(cellsPack -> {
                double amountToTransfer = mine.tokenPerCell * cellsPack.quantity.doubleValue();
                unitInterface.sendTokens(new TransferTokensRequests());
                Optional<JSONObject> tokenDistribution = PostRequest.sendFromMerchantTokensOptional(cellsPack.nftName, amountToTransfer, mine.tokenName);
                tokenDistribution = PostRequest.sendFromMerchantTokensOptional(cellsPack.nftName, amountToTransfer, mine.tokenName);
                scheduledTransactionArrayList.add(new ScheduledTransaction(new Date().getTime() + Constants.FIVE_DAYS_MS, tokenDistribution.get().getString("hash"), cellsPack.nftName, amountToTransfer, mine.tokenName, false));
                try {
                    TimeUnit.SECONDS.sleep(3);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                cellsPack.setStatus("reverted");
                cellsPackList.add(cellsPack);
            });
            cellsPackRepository.saveAll(cellsPackList);
        });
        Executor firstHalfExecutor = (runnable) -> new Thread(runnable).start();
        firstHalfExecutor.execute(firstHalf);
        mine.setStatus(false);
        mineRepository.save(mine);
        String tokenName = StringUtils.capitalize(mine.tokenName);
        return ResponseHandler.generateResponse(String.format("%s has been distributed successfully. All %s(-s) will be soon available to their holders.", tokenName, tokenName), HttpStatus.OK, null);
    }
}
