package com.client.eurekaclient.utilities.http;

import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class PostRequest {
    private static final RestTemplate restTemplate = new RestTemplate();
    private HttpHeaders headers = new HttpHeaders();
//    public static String UNIT = "http://localhost:49000"; // not  for production
    public static String UNIT = "http://localhost:29000"; // for production
//    public static String UNIT = "http://194.156.188.10"; // for tests

    public static JSONObject sendRequest(String url, HashMap<String, Object> parameters) {
        String strResponse = restTemplate.postForObject(url, parameters, String.class, new HashMap<>());
        return new JSONObject(strResponse);
    }

    public static JSONObject sendUnitTransaction(String to, String from, double amount) {
        HashMap<String, Object> data = new HashMap<>();
        HashMap<String, String> extradata = new HashMap<>();
        data.put("from", from);
        data.put("to", to);
        data.put("amount", amount);
        data.put("type", 0);
        extradata.put("name", "null");
        extradata.put("value", "null");
        extradata.put("bytecode", "null");
        data.put("extradata", extradata);
        return PostRequest.sendRequest(PostRequest.UNIT, new HashMap<>(Map.of("instruction", "i_push_transaction", "data", data)));
    }

    public static JSONObject sendTokenTransaction(String to, String from, double amount, String token) {
        HashMap<String, Object> data = new HashMap<>();
        HashMap<String, String> extradata = new HashMap<>();
        data.put("from", from);
        data.put("to", to);
        data.put("amount", 0);
        data.put("type", 2);
        extradata.put("name", token);
        extradata.put("value", String.valueOf(amount));
        extradata.put("bytecode", "null");
        data.put("extradata", extradata);
        return PostRequest.sendRequest(PostRequest.UNIT, new HashMap<>(Map.of("instruction", "i_push_transaction", "data", data)));
    }

    public static JSONObject sendToMerchantTokens(String from, double amount, String token) {
        HashMap<String, Object> data = new HashMap<>();
        HashMap<String, String> extradata = new HashMap<>();
        data.put("from", from);
        data.put("to", "merchant");
        data.put("amount", 0);
        data.put("type", 2);
        extradata.put("name", token);
        extradata.put("value", String.valueOf(amount));
        extradata.put("bytecode", "null");
        data.put("extradata", extradata);
        return PostRequest.sendRequest(PostRequest.UNIT, new HashMap<>(Map.of("instruction", "i_push_transaction", "data", data)));
    }

    public static JSONObject sendFromMerchantTokens(String to, double amount, String token) {
        HashMap<String, Object> data = new HashMap<>();
        HashMap<String, String> extradata = new HashMap<>();
        data.put("from", "merchant");
        data.put("to", to);
        data.put("amount", 0);
        data.put("type", 2);
        extradata.put("name", token);
        extradata.put("value", String.valueOf(amount));
        extradata.put("bytecode", "null");
        data.put("extradata", extradata);
        return PostRequest.sendRequest(PostRequest.UNIT, new HashMap<>(Map.of("instruction", "i_push_transaction", "data", data)));
    }
    public static Optional<JSONObject> sendFromMerchantTokensOptional(String to, double amount, String token) {
        HashMap<String, Object> data = new HashMap<>();
        HashMap<String, String> extradata = new HashMap<>();
        data.put("from", "merchant");
        data.put("to", to);
        data.put("amount", 0);
        data.put("type", 2);
        extradata.put("name", token);
        extradata.put("value", String.valueOf(amount));
        extradata.put("bytecode", "null");
        data.put("extradata", extradata);
        JSONObject jsonObject;
        try {
            jsonObject = PostRequest.sendRequest(PostRequest.UNIT, new HashMap<>(Map.of("instruction", "i_push_transaction", "data", data)));
        } catch (Exception e) {
            return Optional.empty();
        }
        return Optional.of(jsonObject);
    }

    public static JSONObject createTokenTransaction(String bytecode) {
        HashMap<String, Object> data = new HashMap<>();
        HashMap<String, String> extradata = new HashMap<>();
        data.put("from", "merchant");
        data.put("to", "");
        data.put("amount", 0);
        data.put("type", 1);
        extradata.put("name", "null");
        extradata.put("value", "null");
        extradata.put("bytecode", bytecode);
        data.put("extradata", extradata);
        return PostRequest.sendRequest(PostRequest.UNIT, new HashMap<>(Map.of("instruction", "i_push_transaction", "data", data)));
    }

    public static JSONObject getBalance(String name) {
        HashMap<String, String> data = new HashMap<>();
        data.put("name", name);
        return PostRequest.sendRequest(PostRequest.UNIT, new HashMap<>(Map.of("instruction", "i_balance", "data", data)));
    }
    public static Optional<JSONObject> getOptionalBalance(String name) {
        HashMap<String, String> data = new HashMap<>();
        data.put("name", name);
        JSONObject jsonObject;
        try {
            jsonObject = PostRequest.sendRequest(PostRequest.UNIT, new HashMap<>(Map.of("instruction", "i_balance", "data", data)));
        } catch (Exception e) {
            return Optional.empty();
        }
        return Optional.of(jsonObject);
    }
    public static Optional<JSONObject> getOptionalPoolSize() {
        JSONObject jsonObject;
        try {
            jsonObject = PostRequest.sendRequest(PostRequest.UNIT, new HashMap<>(Map.of("instruction", "i_pool_size")));
        } catch (Exception e) {
            return Optional.empty();
        }
        return Optional.of(jsonObject);
    }
}