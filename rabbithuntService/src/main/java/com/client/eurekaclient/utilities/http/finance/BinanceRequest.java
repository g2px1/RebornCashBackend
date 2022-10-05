package com.client.eurekaclient.utilities.http.finance;

import com.client.eurekaclient.utilities.http.GetRequest;
import org.json.JSONObject;

public class BinanceRequest extends GetRequest {
    public static double getBNBUSDtPrice() {
        JSONObject jsonObject = new JSONObject(restTemplate.getForObject("https://api.binance.com/api/v3/ticker/price?symbol=BNBUSDT", String.class));
        return jsonObject.getDouble("price");
    }
}
