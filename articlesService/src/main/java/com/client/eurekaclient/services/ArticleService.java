package com.client.eurekaclient.services;

import com.client.eurekaclient.messages.ErrorMessage;
import com.client.eurekaclient.messages.Errors;
import com.client.eurekaclient.models.articles.Article;
import com.client.eurekaclient.models.response.ResponseHandler;
import com.client.eurekaclient.repositories.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class ArticleService {
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private Errors errors;

    public ResponseEntity<Object> findByUuid(String uuid) {
        Optional<Article> articleOptional = articleRepository.findByUuid(uuid);
        if (articleOptional.isEmpty()) return ResponseHandler.generateResponse(errors.NFT_NOT_FOUND, HttpStatus.BAD_REQUEST, null);
        return ResponseHandler.generateResponse(null, HttpStatus.OK, articleOptional.get());
    }
    public ResponseEntity<Object> findPage(int pageNumber, String type) {
        Pageable paging = PageRequest.of(pageNumber, 5);
        Page<Article> page = articleRepository.findAllByType(type, paging);
        return ResponseHandler.generateResponse(null, HttpStatus.OK, Map.of("content", page.getContent(), "currentPage", page.getNumber(), "totalItems", page.getTotalElements(), "totalPages", page.getTotalPages()));
    }
    public ResponseEntity<Object> save(Article article) {
        if (articleRepository.existsByHeading(article.getHeading())) return ResponseHandler.generateResponse(errors.ARTICLE_EXISTS, HttpStatus.BAD_REQUEST, null);
        articleRepository.save(article);
        return ResponseHandler.generateResponse("success", HttpStatus.NO_CONTENT, null);
    }
    public ResponseEntity<Object> update(Article article) {
        Optional<Article> inDBArticle = articleRepository.findByUuid(article.getUuid());
        if (inDBArticle.isEmpty()) return ResponseHandler.generateResponse(errors.ARTICLE_NOT_EXISTS, HttpStatus.BAD_REQUEST, null);
        inDBArticle.get().buildFromOther(article);
        articleRepository.save(inDBArticle.get());
        return ResponseHandler.generateResponse("success", HttpStatus.NO_CONTENT, null);
    }
}
