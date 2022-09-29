package com.client.eurekaclient.controllers;

import com.client.eurekaclient.models.market.AbstractProduct;
import com.client.eurekaclient.models.request.ProductSeekingRequest;
import com.client.eurekaclient.services.MarketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/marketController")
public class MarketController {
    @Autowired
    private MarketService marketService;

    @PostMapping("/page")
    public ResponseEntity<Object> getArticlePage(@RequestBody ProductSeekingRequest productSeekingRequest) {return marketService.findPage(productSeekingRequest);}
    @PostMapping("/findByUuid")
    public ResponseEntity<Object> findByUuid(@RequestBody ProductSeekingRequest productSeekingRequest) {return marketService.findByUUid(productSeekingRequest);}
    @PostMapping("/findAbstractByUuid")
    public Optional<AbstractProduct> findAbstractByUuidAndStatus(@RequestBody ProductSeekingRequest productSeekingRequest) {return marketService.findAbstractByUUid(productSeekingRequest);}
    @PostMapping("/save")
    public <T extends AbstractProduct> ResponseEntity<Object> save(@RequestBody T abstractProduct) {return marketService.save(abstractProduct);}
}
