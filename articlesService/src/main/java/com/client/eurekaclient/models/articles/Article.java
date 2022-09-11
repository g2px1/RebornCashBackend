package com.client.eurekaclient.models.articles;


import jakarta.validation.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Document(collection = "articles")
public class Article implements Comparable<Article>{
    @Id
    private String id;
    @NotBlank
    private String username;
    @NotBlank
    private String shortDescription;
    private List<String> tags;
    @NotBlank
    private String type;
    @NotBlank
    private String heading;
    @NotBlank
    private String articleText;
    @NotBlank
    private String previewImage;
    private String uuid = UUID.randomUUID().toString();
    public String publicationDate = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss").format(LocalDateTime.now());

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getShortDescription() {
        return shortDescription;
    }
    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }
    public List<String> getTags() {
        return tags;
    }
    public void setTags(List<String> tags) {
        this.tags = tags;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getHeading() {
        return heading;
    }
    public void setHeading(String heading) {
        this.heading = heading;
    }
    public String getArticleText() {
        return articleText;
    }
    public void setArticleText(String articleText) {
        this.articleText = articleText;
    }
    public String getPreviewImage() {
        return previewImage;
    }
    public void setPreviewImage(String previewImage) {
        this.previewImage = previewImage;
    }
    public String getUuid() {return uuid;}
    public void setUuid(String uuid) {this.uuid = uuid;}
    public String getPublicationDate() {
        return publicationDate;
    }
    public void setPublicationDate(String publicationDate) {
        this.publicationDate = publicationDate;
    }

    @Override
    public int compareTo(Article o) {
        return this.publicationDate.compareTo(o.publicationDate);
    }
}
