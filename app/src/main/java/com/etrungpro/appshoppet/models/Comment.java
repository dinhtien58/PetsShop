package com.etrungpro.appshoppet.models;

import com.google.firebase.firestore.FieldValue;

import java.sql.Timestamp;

public class Comment {
    String userId;
    String productId;
    FieldValue createAt;
    String content;

    public Comment() {

    }

    public Comment(String userId, String productId, FieldValue createAt, String content) {
        this.userId = userId;
        this.productId = productId;
        this.createAt = createAt;
        this.content = content;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public FieldValue getUpdateAt() {
        return createAt;
    }

    public void setCreateAt(FieldValue createAt) {
        this.createAt = createAt;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
