package com.client.eurekaclient.utilities.http.unit;

import org.json.JSONObject;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

public class PostRequest {
    private static final RestTemplate restTemplate = new RestTemplate();
    public static String UNIT = "http://localhost:29000";

    public static JSONObject sendRequest(String url, HashMap<String, Object> parameters) {
        String strResponse = restTemplate.postForObject(url, parameters, String.class, new HashMap<>());
        return new JSONObject(strResponse);
    }
    public static JSONObject sendUnitTransaction(String to, String from, double amount) {
        HashMap<String, Object> data = new HashMap<>();
        HashMap<String, String> extraData = new HashMap<>();
        data.put("from", from);
        data.put("to", to);
        data.put("amount", amount);
        data.put("type", 0);
        extraData.put("name", "null");
        extraData.put("value", "null");
        extraData.put("bytecode", "null");
        data.put("extradata", extraData);
        return PostRequest.sendRequest(PostRequest.UNIT, new HashMap<>(Map.of("instruction", "i_push_transaction", "data", data)));
    }
    public static JSONObject sendTokenTransaction(String to, String from, double amount, String token) {
        HashMap<String, Object> data = new HashMap<>();
        HashMap<String, String> extraData = new HashMap<>();
        data.put("from", from);
        data.put("to", to);
        data.put("amount", 0);
        data.put("type", 2);
        extraData.put("name", token);
        extraData.put("value", String.valueOf(amount));
        extraData.put("bytecode", "null");
        data.put("extradata", extraData);
        return PostRequest.sendRequest(PostRequest.UNIT, new HashMap<>(Map.of("instruction", "i_push_transaction", "data", data)));
    }
    public static JSONObject sendToMerchantTokens(String from, double amount, String token) {
        HashMap<String, Object> data = new HashMap<>();
        HashMap<String, String> extraData = new HashMap<>();
        data.put("from", from);
        data.put("to", "merchant");
        data.put("amount", 0);
        data.put("type", 2);
        extraData.put("name", token);
        extraData.put("value", String.valueOf(amount));
        extraData.put("bytecode", "null");
        data.put("extradata", extraData);
        return PostRequest.sendRequest(PostRequest.UNIT, new HashMap<>(Map.of("instruction", "i_push_transaction", "data", data)));
    }
    public static JSONObject sendFromMerchantTokens(String to, double amount, String token) {
        HashMap<String, Object> data = new HashMap<>();
        HashMap<String, String> extraData = new HashMap<>();
        data.put("from", "merchant");
        data.put("to", to);
        data.put("amount", 0);
        data.put("type", 2);
        extraData.put("name", token);
        extraData.put("value", String.valueOf(amount));
        extraData.put("bytecode", "null");
        data.put("extradata", extraData);
        return PostRequest.sendRequest(PostRequest.UNIT, new HashMap<>(Map.of("instruction", "i_push_transaction", "data", data)));
    }
    public static JSONObject createTokenTransaction(String bytecode) {
        HashMap<String, Object> data = new HashMap<>();
        HashMap<String, String> extraData = new HashMap<>();
        data.put("from", "merchant");
        data.put("to", "");
        data.put("amount", 0);
        data.put("type", 1);
        extraData.put("name", "null");
        extraData.put("value", "null");
        extraData.put("bytecode", bytecode);
        data.put("extradata", extraData);
        return PostRequest.sendRequest(PostRequest.UNIT, new HashMap<>(Map.of("instruction", "i_push_transaction", "data", data)));
    }
    public static JSONObject getBalance(String name) {
        HashMap<String, String> data = new HashMap<>();
        data.put("name", name);
        return PostRequest.sendRequest(PostRequest.UNIT, new HashMap<>(Map.of("instruction", "i_balance", "data", data)));
    }
    public static JSONObject findTransaction(String hash) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("hash", hash);
        return PostRequest.sendRequest(PostRequest.UNIT, new HashMap<>(Map.of("instruction", "i_tx", "data", data)));
    }
    public static JSONObject blockHeight() {
        return PostRequest.sendRequest(PostRequest.UNIT, new HashMap<>(Map.of("instruction", "i_block_height")));
    }
    public static JSONObject poolSize() {
        return PostRequest.sendRequest(PostRequest.UNIT, new HashMap<>(Map.of("instruction", "i_pool_size")));
    }
}