package com.client.eurekaclient.services;

import com.client.eurekaclient.messages.ErrorMessage;
import com.client.eurekaclient.models.market.AbstractProduct;
import com.client.eurekaclient.models.market.rabbithunt.CellsPack;
import com.client.eurekaclient.models.market.rabbithunt.Token;
import com.client.eurekaclient.models.request.ProductSeekingRequest;
import com.client.eurekaclient.models.response.ResponseHandler;
import com.client.eurekaclient.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class MarketService {
    @Autowired
    private ProductRepository productRepository;

    public ResponseEntity<Object> findPage(ProductSeekingRequest productSeekingRequest) {
        Sort.Direction direction = (productSeekingRequest.direction) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(productSeekingRequest.page, 10, Sort.by(direction, "price"));
        if (productSeekingRequest.type != null) {
            if (productSeekingRequest.trapName != null) {
                System.out.println(productSeekingRequest.trapName);
                pageable = PageRequest.of(productSeekingRequest.page, 10, direction, productSeekingRequest.trapName);
                Page<AbstractProduct> page = productRepository.findByTypeAndStatus(productSeekingRequest.type, true, pageable);
                return ResponseHandler.generateResponse(null, HttpStatus.OK, Map.of("content", page.getContent(), "currentPage", page.getNumber(), "totalItems", page.getTotalElements(), "totalPages", page.getTotalPages()));
            }
            if (productSeekingRequest.tokenName != null) {
                pageable = PageRequest.of(productSeekingRequest.page, 10, direction, productSeekingRequest.tokenName);
                Page<AbstractProduct> page = productRepository.findByTypeAndStatus(productSeekingRequest.type, true, pageable);
                return ResponseHandler.generateResponse(null, HttpStatus.OK, Map.of("content", page.getContent(), "currentPage", page.getNumber(), "totalItems", page.getTotalElements(), "totalPages", page.getTotalPages()));
            }

            Page<AbstractProduct> page = productRepository.findByTypeAndStatus(productSeekingRequest.type, true, pageable);
            return ResponseHandler.generateResponse(null, HttpStatus.OK, Map.of("content", page.getContent(), "currentPage", page.getNumber(), "totalItems", page.getTotalElements(), "totalPages", page.getTotalPages()));
        }

        Page<AbstractProduct> page = productRepository.findByStatus(true, pageable);
        return ResponseHandler.generateResponse(null, HttpStatus.OK, Map.of("content", page.getContent(), "currentPage", page.getNumber(), "totalItems", page.getTotalElements(), "totalPages", page.getTotalPages()));
    }
    public ResponseEntity<Object> findByUUid(ProductSeekingRequest productSeekingRequest) {
        Optional<AbstractProduct> optionalAbstractProduct = productRepository.findByUuid(productSeekingRequest.uuid);
        if (optionalAbstractProduct.isEmpty())
            return ResponseHandler.generateResponse(ErrorMessage.PRODUCT_NOT_FOUND, HttpStatus.OK, null);
        return ResponseHandler.generateResponse(null, HttpStatus.OK, optionalAbstractProduct.get());
    }
    public ResponseEntity<Object> save(AbstractProduct abstractProduct) {
        productRepository.save(abstractProduct);
        return ResponseHandler.generateResponse("success", HttpStatus.OK, null);
    }
}