package com.etrungpro.appshoppet.models;

import com.google.firebase.Timestamp;

public class Post {
    private String postId;
    private String title;
    private String author;
    private String url;
    private String image;
    private Timestamp published_at;

    public Post(){}

    public Post(String id, String title, String author, String url, String image, Timestamp published_at) {
        this.postId = id;
        this.title = title;
        this.author = author;
        this.url = url;
        this.image = image;
        this.published_at = published_at;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getId() {
        return postId;
    }

    public void setId(String id) {
        this.postId = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Timestamp getPublished_at() {
        return published_at;
    }

    public void setPublished_at(Timestamp published_at) {
        this.published_at = published_at;
    }
}
