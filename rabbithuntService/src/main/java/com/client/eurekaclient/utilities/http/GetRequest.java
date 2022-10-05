package com.client.eurekaclient.utilities.http;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.client.RestTemplate;

public class GetRequest {
    protected static final RestTemplate restTemplate = new RestTemplate();

    public static JSONObject getRequest(String url) {
        return new JSONObject(restTemplate.getForObject(url, String.class));
    }

    public static JSONObject getTokenData(String tokenAddress) {
        return new JSONArray(restTemplate.getForObject( String.format("https://api.benswap.cash/api/dex/token/%s", tokenAddress), String.class)).getJSONObject(0);
    }

    public static String getTokenUSDPrice(String tokenAddress) {
        return new JSONArray(restTemplate.getForObject( String.format("https://api.benswap.cash/api/dex/token/%s", tokenAddress), String.class)).getJSONObject(0).get("priceUsd").toString();
    }

    public static String getTokenBCHPrice(String tokenAddress) {
        return new JSONArray(restTemplate.getForObject( String.format("https://api.benswap.cash/api/dex/token/%s", tokenAddress), String.class)).getJSONObject(0).get("priceBch").toString();
    }
}
