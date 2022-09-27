package com.client.eurekaclient.services.rabbithunt.trap;

import com.client.eurekaclient.constants.Constants;
import com.client.eurekaclient.messages.ErrorMessage;
import com.client.eurekaclient.models.rabbithunt.cells.CellsPack;
import com.client.eurekaclient.models.rabbithunt.tokens.Token;
import com.client.eurekaclient.models.rabbithunt.trap.CellsTransactions;
import com.client.eurekaclient.models.rabbithunt.trap.Trap;
import com.client.eurekaclient.models.request.rabbithunt.trap.CloseTrapRequest;
import com.client.eurekaclient.models.request.rabbithunt.trap.TrapRequest;
import com.client.eurekaclient.models.request.unit.TransferTokensRequests;
import com.client.eurekaclient.models.response.ResponseHandler;
import com.client.eurekaclient.models.scheduled.transactions.ScheduledTransaction;
import com.client.eurekaclient.repositories.*;
import com.client.eurekaclient.services.openfeign.transactions.unit.UnitInterface;
import com.client.eurekaclient.utilities.Upload.StorageService;
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
public class SecuredTrapService {
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
    private TrapRepository trapRepository;
    @Value("${file.upload-dir}")
    private String directory;
    private final StorageService storageService;
    private static final Logger logger = LoggerFactory.getLogger(SecuredTrapService.class);

    @Autowired
    public SecuredTrapService(StorageService storageService) {
        this.storageService = storageService;
    }

    private static String convertToHex(String tokenStructure) {
        return tokenStructure.chars().mapToObj(Integer::toHexString).collect(Collectors.joining());
    }

    public ResponseEntity<Object> createTrap(TrapRequest trapRequest) throws IllegalAccessException {
        if (!tokensRepository.existsByName(trapRequest.tokenName.trim()))
            return ResponseHandler.generateResponse("Token doesn't exist in UNIT.", HttpStatus.BAD_REQUEST, null);
        if (trapRequest.checkNull())
            return ResponseHandler.generateResponse("All fields should be filled in.", HttpStatus.BAD_REQUEST, null);
        if (trapRepository.existsByName(StringUtils.capitalize(trapRequest.name.toLowerCase(Locale.ROOT)).trim()))
            return ResponseHandler.generateResponse("Trap exists.", HttpStatus.BAD_REQUEST, null);
        if (!YahooFinanceRequest.optionExists(trapRequest.optionName.toUpperCase(Locale.ROOT).trim()))
            return ResponseHandler.generateResponse("Option doesn't exist on Yahoo finance.", HttpStatus.BAD_REQUEST, null);
        if (new File(directory, trapRequest.imageFile.getOriginalFilename()).exists())
            return ResponseHandler.generateResponse("File exists", HttpStatus.BAD_REQUEST, null);
        if (trapRequest.activeTill == -1)
            return ResponseHandler.generateResponse("'activeTill' should be filled in", HttpStatus.BAD_REQUEST, null);
        storageService.store(trapRequest.imageFile);
        double optionPrice = YahooFinanceRequest.getOptionOptionalRegularMarketPrice(trapRequest.optionName) * 100;
        trapRepository.save(new Trap(StringUtils.capitalize(trapRequest.name.toLowerCase(Locale.ROOT)).trim(), UUID.randomUUID().toString(), trapRequest.imageFile.getOriginalFilename().trim(), trapRequest.cells, trapRequest.created, trapRequest.activeAfter, trapRequest.optionType.trim(), trapRequest.tools.trim(), Math.round(trapRequest.strikePrice), trapRequest.cells, trapRequest.tokenName.trim(), trapRequest.tokenTotalWeight, ((optionPrice * trapRequest.quantityOfLots) / trapRequest.cells), trapRequest.optionName.trim(), true, trapRequest.activeTill, trapRequest.quantityOfLots));
        PostRequest.createTokenTransaction(convertToHex(String.format("{\"name\": \"%s\", \"supply\": %s}", trapRequest.name.toLowerCase(Locale.ROOT).trim(), trapRequest.cells)));
        tokensRepository.save(new Token(trapRequest.name.toLowerCase(Locale.ROOT).trim(), BigDecimal.valueOf(trapRequest.cells), (short) 1));
        return ResponseHandler.generateResponse("ok", HttpStatus.OK, true);
    }

    public ResponseEntity<Object> editTrap(TrapRequest trapRequest) {
        if (!trapRepository.existsByName(StringUtils.capitalize(trapRequest.name.toLowerCase(Locale.ROOT))))
            return ResponseHandler.generateResponse("Trap not exists.", HttpStatus.BAD_REQUEST, null);
        Trap trap = trapRepository.findByName(StringUtils.capitalize(trapRequest.name.toLowerCase(Locale.ROOT)));
        if (trapRequest.cells != 0 && trapRequest.cells < trap.cells)
            return ResponseHandler.generateResponse("Cells quantity can't be changed to lower one", HttpStatus.BAD_REQUEST, null);
        else {
            trap.setCellsAvailable(trapRequest.cells - trap.cells + trap.cellsAvailable);
            trap.setCells(trapRequest.cells);
        }
        if (!trap.name.equalsIgnoreCase(trapRequest.name))
            return ResponseHandler.generateResponse("Trap name can't be changed", HttpStatus.BAD_REQUEST, null);
        if (!trap.optionName.equalsIgnoreCase(trapRequest.optionName))
            return ResponseHandler.generateResponse("Option name can't be changed", HttpStatus.BAD_REQUEST, null);
        trap.updateFromRequest(trapRequest);
        trapRepository.save(trap);
        return ResponseHandler.generateResponse(null, HttpStatus.OK, true);
    }

    public ResponseEntity<Object> loadTrap(HashMap<String, String> findTrap) {
        return ResponseHandler.generateResponse(null, HttpStatus.OK, trapRepository.findByName(findTrap.get("trapName")));
    }

    public ResponseEntity<Object> loadTrapsImages() {
        return ResponseHandler.generateResponse(null, HttpStatus.OK, Stream.of(Objects.requireNonNull(new File(directory).listFiles()))
                .filter(file -> !file.isDirectory())
                .map(File::getName)
                .collect(Collectors.toSet()));
    }

    public ResponseEntity<Object> getTrapsImage(String trapImage) {
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

    public ResponseEntity<Object> loadTraps(HashMap<String, Integer> pageNumber) {
        Pageable paging = PageRequest.of(pageNumber.get("pageNumber"), 5);
        Page<Trap> page = trapRepository.findAllByStatus("active", paging);
        return ResponseHandler.generateResponse(null, HttpStatus.OK, Map.of("content", page.getContent(), "currentPage", page.getNumber(), "totalItems", page.getTotalElements(), "totalPages", page.getTotalPages()));
    }

    public ResponseEntity<Object> closeTrap(CloseTrapRequest closeTrapRequest) {
        if (!trapRepository.existsByName(closeTrapRequest.trapName))
            return ResponseHandler.generateResponse(ErrorMessage.TRAP_NOT_EXIST, HttpStatus.OK, false);
        Trap trap = trapRepository.findByName(closeTrapRequest.trapName);
        if (!trap.status) return ResponseHandler.generateResponse(ErrorMessage.TRAP_CLOSED, HttpStatus.OK, false);
        try {
            if (closeTrapRequest.price != null) trap.setTokenPerCell(Double.parseDouble(closeTrapRequest.price) / trap.cells);
            else trap.setTokenPerCell(YahooFinanceRequest.getOptionOptionalRegularMarketPrice(trap.optionName) * 100 / trap.cells);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        Optional<List<CellsTransactions>> optionalTrapsCellsTransactionsList = cellsTransactionRepository.findByTrap(trap);
        if (optionalTrapsCellsTransactionsList.isEmpty()) {
            trap.setStatus(false);
            trapRepository.save(trap);
            return ResponseHandler.generateResponse(ErrorMessage.TRAP_IS_FULL, HttpStatus.OK, null);
        }

        Runnable firstHalf = (() -> {
            List<ScheduledTransaction> layer1ExpiringTransactions = new ArrayList<>();
            List<CellsTransactions> cellsTransactions = optionalTrapsCellsTransactionsList.get();
            for (int i = 0; i < cellsTransactions.size(); i++) {
                double amountToTransfer = trap.tokenPerCell * cellsTransactions.get(i).quantity;
                JSONObject tokenDistribution = unitInterface.sendTokens(new TransferTokensRequests(cellsTransactions.get(i).nftName, "merchant", amountToTransfer, trap.tokenName));
                layer1ExpiringTransactions.add(new ScheduledTransaction(Instant.now().plusMillis(Constants.FIVE_DAYS_MS).toEpochMilli(), tokenDistribution.getString("hash"), cellsTransactions.get(i).nftName, amountToTransfer, trap.tokenName, false));
                try {
                    TimeUnit.SECONDS.sleep(3);
                } catch (Exception e) {
                    logger.error(e.getMessage());
                }
            }
            scheduledTransactionRepository.saveAll(layer1ExpiringTransactions);
            List<CellsPack> cellsPackList = new ArrayList<>();
            cellsPackRepository.findByTrap(trap).forEach(cellsPack -> {
                double amountToTransfer = trap.tokenPerCell * cellsPack.quantity.doubleValue();
                unitInterface.sendTokens(new TransferTokensRequests());
                Optional<JSONObject> tokenDistribution = PostRequest.sendFromMerchantTokensOptional(cellsPack.nftName, amountToTransfer, trap.tokenName);
                tokenDistribution = PostRequest.sendFromMerchantTokensOptional(cellsPack.nftName, amountToTransfer, trap.tokenName);
                layer1ExpiringTransactions.add(new ScheduledTransaction(new Date().getTime() + Constants.FIVE_DAYS_MS, tokenDistribution.get().getString("hash"), cellsPack.nftName, amountToTransfer, trap.tokenName, false));
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
        trap.setStatus(false);
        trapRepository.save(trap);
        String tokenName = StringUtils.capitalize(trap.tokenName);
        return ResponseHandler.generateResponse(String.format("%s has been distributed successfully. All %s(-s) will be soon available to their holders.", tokenName, tokenName), HttpStatus.OK, null);
    }
}
