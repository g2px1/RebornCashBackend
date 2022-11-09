package com.client.eurekaclient.repositories;

import com.client.eurekaclient.models.articles.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends MongoRepository<Article, String> {
    Optional<Article> findByUuid(String uuid);
    Page<Article> findAllByType(String type, Pageable pageable);
    List<Article> findAll();
    boolean existsByHeading(String heading);

    boolean existsByUuid(String uuid);

    void deleteArticleByUuid(String uuid);
}
