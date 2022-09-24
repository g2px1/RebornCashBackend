package com.client.eurekaclient.repositories;

import com.client.eurekaclient.models.rabbithunt.tokens.Token;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TokensRepository extends MongoRepository<Token, String> {
    Token findByName(String name);
    boolean existsByName(String name);
}
