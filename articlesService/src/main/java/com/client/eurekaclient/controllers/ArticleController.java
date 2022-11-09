package com.client.eurekaclient.controllers;

import com.client.eurekaclient.models.articles.Article;
import com.client.eurekaclient.models.request.ArticlePageRequest;
import com.client.eurekaclient.models.request.ArticleSeekingRequest;
import com.client.eurekaclient.services.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/articleService")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    @PostMapping("/page")
    public ResponseEntity<Object> getArticlePage(@RequestBody ArticlePageRequest articlePageRequest) {
        return articleService.findPage(articlePageRequest.pageNumber, articlePageRequest.type);
    }
    @PostMapping("/findByUuid")
    public ResponseEntity<Object> findByUuid(@RequestBody ArticleSeekingRequest articleSeekingRequest) {
        System.out.println(articleSeekingRequest.uuid);
        return articleService.findByUuid(articleSeekingRequest.uuid);
    }
    @PostMapping("/save")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Object> save(@RequestBody Article article) {
        return articleService.save(article);
    }
    @PostMapping("/update")
//    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Object> update(@RequestBody Article article) {
        return articleService.update(article);
    }
}
