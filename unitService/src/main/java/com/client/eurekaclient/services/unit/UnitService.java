package com.client.eurekaclient.services.unit;

import com.client.eurekaclient.models.DTO.unit.TransferTokensRequests;
import com.client.eurekaclient.utilities.http.unit.PostRequest;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class UnitService {
    public JSONObject sendTokens(TransferTokensRequests transferTokensRequests) {
        try {
            if (transferTokensRequests.sender.equals("merchant")) {
                return PostRequest.sendFromMerchantTokens(transferTokensRequests.recipient, Double.parseDouble(transferTokensRequests.amount), transferTokensRequests.tokenName);
            }
            if (transferTokensRequests.recipient.equals("merchant")) {
                return PostRequest.sendToMerchantTokens(transferTokensRequests.sender, Double.parseDouble(transferTokensRequests.amount), transferTokensRequests.tokenName);
            }
            return new JSONObject("""
                        {"error": "invalid transaction"}
                    """);
        } catch (Exception e) {
            return new JSONObject("""
                        {"error": "error occurred"}
                    """);
        }
    }
}
