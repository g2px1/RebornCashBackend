package com.client.eurekaclient.repositories;

import com.client.eurekaclient.models.rabbithunt.tokens.Token;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TokensRepository extends MongoRepository<Token, String> {
    Token findByName(String name);
    boolean existsByName(String name);
    Page<Token> findAllByTokenType(String tokenType, Pageable pageable);
}
