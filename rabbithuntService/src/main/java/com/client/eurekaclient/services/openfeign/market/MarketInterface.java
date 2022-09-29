package com.client.eurekaclient.services.openfeign.market;

import com.client.eurekaclient.models.market.AbstractProduct;
import com.client.eurekaclient.models.request.market.ProductSeekingRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@FeignClient(name = "marketplaceService")
public interface MarketInterface {
    @PostMapping("/save")
    <T extends AbstractProduct> ResponseEntity<Object> save(@RequestBody T abstractProduct);
    @PostMapping("/findAbstractByUuid")
    <T extends AbstractProduct> Optional<T> findAbstractByUuidAndStatus(@RequestBody ProductSeekingRequest productSeekingRequest);
}
