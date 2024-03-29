package com.client.eurekaclient.services.unit;

import com.client.eurekaclient.models.request.unit.TransferTokensRequests;
import com.client.eurekaclient.utilities.unit.Unit;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UnitService {
    private static final Logger logger = LoggerFactory.getLogger(UnitService.class);
    public String errorContainer = """
                        {"error": "%s"}
                    """;
    public JSONObject sendTokens(TransferTokensRequests transferTokensRequests) {
        try {
            if (transferTokensRequests.sender.equals("merchant")) {
                return Unit.sendFromMerchantTokens(transferTokensRequests.recipient, transferTokensRequests.amount, transferTokensRequests.tokenName);
            }
            if (transferTokensRequests.recipient.equals("merchant")) {
                return Unit.sendToMerchantTokens(transferTokensRequests.sender, transferTokensRequests.amount, transferTokensRequests.tokenName);
            }
            return new JSONObject(String.format(errorContainer, "invalid transaction"));
        } catch (Exception e) {
            return new JSONObject(String.format("error occurred: undefined"));
        }
    }
    public JSONObject sendUnits(TransferTokensRequests transferTokensRequests) {
        try {
            return Unit.sendUnitTransaction(transferTokensRequests.recipient, transferTokensRequests.sender, transferTokensRequests.amount);
        } catch (Exception e) {
            return new JSONObject(String.format("error occurred: undefined"));
        }
    }
    public JSONObject createToken(String bytecode) {
        try {
            return Unit.createTokenTransaction(bytecode);
        } catch (Exception e) {
            return new JSONObject(String.format("error occurred: low balance or token exists"));
        }
    }
    public JSONObject findTx(String hash) {
        try {
            return Unit.findTransaction(hash);
        } catch (Exception e) {
            return new JSONObject(String.format("error occurred: not found"));
        }
    }
    public JSONObject getBalance(String address) {
        try {
            return Unit.getBalance(address);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new JSONObject();
        }
    }
    public JSONObject getBlockHeight() {
        try {
            return Unit.blockHeight();
        } catch (Exception e) {
            return new JSONObject(String.format("error occurred: undefined"));
        }
    }
    public JSONObject getTxPool() {
        try {
            return Unit.poolSize();
        } catch (Exception e) {
            return new JSONObject(String.format("error occurred: undefined"));
        }
    }
}
