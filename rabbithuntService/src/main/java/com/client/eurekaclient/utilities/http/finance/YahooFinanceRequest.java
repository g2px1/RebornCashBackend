package com.client.eurekaclient.utilities.http.finance;

import com.client.eurekaclient.utilities.http.GetRequest;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Locale;
import java.util.Optional;

public class YahooFinanceRequest extends GetRequest {
    public static final String yahooUrl = "https://query2.finance.yahoo.com/v10/finance/quoteSummary/%s?modules=price";
    public static Optional<JSONObject> getOptionPrice(String optionName) {
        JSONObject response = new JSONObject();
        try {
            response = getRequest(String.format(yahooUrl, optionName.toUpperCase(Locale.ROOT)));
        } catch (Exception e) {
            String reason;
            try {
                reason = e.getMessage().split("404 Not Found:")[1].substring(2);
                reason = reason.substring(0, reason.length()-1);
            } catch (Exception ex) {
                reason = "null";
            }
            response = new JSONObject( String.format("{\"error\": true, \"reason\": %s}", reason)); // , \"reason\": %s}", reason
        }

        return Optional.of(response);
    }

    public static boolean optionExists(String optionName) {
        try {
            getRequest(String.format(yahooUrl, optionName.toUpperCase(Locale.ROOT)));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static Double getOptionOptionalRegularMarketPrice(String optionName) {
        Optional<JSONObject> optionalPrice = YahooFinanceRequest.getOptionPrice(optionName);
        if(optionalPrice.isEmpty())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "trap not exists");
        double price;
        try {
            price = optionalPrice.get().getJSONObject("quoteSummary").getJSONArray("result").getJSONObject(0).getJSONObject("price").getJSONObject("regularMarketPrice").getDouble("raw");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "trap not exists");
        }
        return price;
    }
}
