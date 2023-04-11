package com.etrungpro.appshoppet.models;

import com.google.firebase.Timestamp;

public class Order {
    String id;
    String overview;
    String address;
    int totalPrice;
    String userId;
    Timestamp createAt;


    public Order() {
    }

    public Order(String id, String overview, String address, int totalPrice, String userId, Timestamp createAt) {
        this.id = id;
        this.overview = overview;
        this.address = address;
        this.totalPrice = totalPrice;
        this.userId = userId;
        this.createAt = createAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Timestamp getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Timestamp createAt) {
        this.createAt = createAt;
    }
}
