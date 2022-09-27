package com.client.eurekaclient.services.rabbithunt.transaction;

import com.client.eurekaclient.messages.ErrorMessage;
import com.client.eurekaclient.models.rabbithunt.tokens.Token;
import com.client.eurekaclient.models.request.rabbithunt.token.CreateTokenRequest;
import com.client.eurekaclient.models.request.rabbithunt.token.TokenSeekingRequest;
import com.client.eurekaclient.models.request.unit.TransferTokensRequests;
import com.client.eurekaclient.models.response.ResponseHandler;
import com.client.eurekaclient.repositories.TokensRepository;
import com.client.eurekaclient.services.openfeign.transactions.unit.UnitInterface;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SecuredTxService {
    @Autowired
    private TokensRepository tokensRepository;
    @Autowired
    private UnitInterface unitInterface;

    public ResponseEntity<Object> createToken(@RequestBody CreateTokenRequest createTokenRequest) throws IllegalAccessException {
        if(createTokenRequest.checkNull())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "All fields should be filled in.");

        if(createTokenRequest.type > 1 || createTokenRequest.type < 0)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid type value.");

        if(tokensRepository.existsByName(createTokenRequest.name.toLowerCase(Locale.ROOT)))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token exist.");

        tokensRepository.save(new Token(createTokenRequest.name.toLowerCase(Locale.ROOT), createTokenRequest.supply, createTokenRequest.type, "token"));
        JSONObject response = unitInterface.createToken(convertToHex(String
                .format("""
                                {"name": "%s", "supply": %s}
                                """, createTokenRequest.name.toLowerCase(Locale.ROOT), createTokenRequest.supply)));
        return ResponseHandler.generateResponse(null, HttpStatus.OK, response.get("hash"));
    }

    public ResponseEntity<Object> distributeToken(TransferTokensRequests transferTokensRequests) {
        if(!tokensRepository.existsByName(transferTokensRequests.tokenName)) return ResponseHandler.generateResponse(ErrorMessage.TOKEN_NOT_EXISTS, HttpStatus.OK, null);
        Optional<JSONObject> merchantBalance = unitInterface.getBalance("merchant");
        Optional<String> balance = Arrays.stream(merchantBalance.get().get("tokens_balance").toString().replaceAll("[\\[\\]]", "").split(",")).filter(value -> value.replaceAll("[\\{\\}]", "").split(":")[0].replaceAll("[\"]", "").equals(transferTokensRequests.tokenName)).findAny();
        if(balance.isEmpty()) return ResponseHandler.generateResponse(ErrorMessage.DEFAULT_ERROR, HttpStatus.OK, null);
        BigDecimal amount = new BigDecimal(balance.get().split(":")[1].replace("}", ""));
        if(amount.compareTo(BigDecimal.valueOf(transferTokensRequests.amount)) < 0) return ResponseHandler.generateResponse(ErrorMessage.LOW_TOKEN_BALANCE, HttpStatus.OK, null);
        return ResponseHandler.generateResponse(null, HttpStatus.OK, unitInterface.sendTokens(new TransferTokensRequests(transferTokensRequests.recipient, "merchant", transferTokensRequests.amount, transferTokensRequests.tokenName)).toMap());
    }

    public ResponseEntity<Object> balanceOf(String address) {
        Optional<JSONObject> optionalJSONObject = unitInterface.getBalance(address);
        if (optionalJSONObject.isEmpty()) return ResponseHandler.generateResponse(ErrorMessage.BALANCE_NOT_FOUND, HttpStatus.OK, null);
        return ResponseHandler.generateResponse(null, HttpStatus.OK, optionalJSONObject.get().toMap());
    }

    public ResponseEntity<Object> loadTokens(TokenSeekingRequest tokenSeekingRequest) {
        Pageable paging = PageRequest.of(tokenSeekingRequest.page, 10);
        Page<Token> page = tokensRepository.findAllByTokenType(tokenSeekingRequest.tokenType, paging);
        return ResponseHandler.generateResponse(null, HttpStatus.OK, Map.of("content", page.getContent(), "currentPage", page.getNumber(), "totalItems", page.getTotalElements(), "totalPages", page.getTotalPages()));
    }

    private static String convertToHex(String tokenStructure) {
        return tokenStructure.chars().mapToObj(Integer::toHexString).collect(Collectors.joining());
    }
}
