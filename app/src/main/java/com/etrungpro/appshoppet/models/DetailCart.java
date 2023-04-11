package com.etrungpro.appshoppet.models;

import java.io.Serializable;

public class DetailCart implements Serializable {
    String id;
    String cartId;
    String productId;
    int productPrice;
    int quatity;

    public DetailCart() {

    }

    public DetailCart(String id, String cartId, String productId, int quatity, int productPrice) {
        this.id = id;
        this.cartId = cartId;
        this.productId = productId;
        this.quatity = quatity;
        this.productPrice = productPrice;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
    }

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getQuatity() {
        return quatity;
    }

    public void setQuatity(int quatity) {
        this.quatity = quatity;
    }
}
