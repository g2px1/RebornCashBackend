package com.client.eurekaclient.utilities.http.finance;

import com.client.eurekaclient.utilities.http.GetRequest;
import org.json.JSONArray;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

public class BenSwapRequest extends GetRequest {
    public static final String benTokenSwapUrl = "https://api.benswap.cash/api/dex/token/";
    private static final RestTemplate restTemplate = new RestTemplate();

    public static Optional<JSONArray> getGameData() {
        JSONArray jsonArray;
        try {
            jsonArray = new JSONArray(restTemplate.getForObject("https://api.benswap.cash/api/dex/token/0xd2597a0bde31Ddec2440E256d8AA35eb63F1A9e3", String.class));
        } catch (Exception e) {
            return Optional.empty();
        }

        return Optional.of(jsonArray);
    }
}
