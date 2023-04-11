package com.etrungpro.appshoppet.models;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class Product {
    private String id;
    private String name;
    private ArrayList<String> img;
    private String imgBig;
    private int price;
    private String description;

    private String category;

    public Product() {

    }

    public Product(String name, String imgBig, ArrayList<String> img, int price, String description, String category) {
        this.name = name;
        this.imgBig = imgBig;
        this.img = img;
        this.price = price;
        this.description = description;
        this.category = category;
    }

    public String getImgBig() {
        return imgBig;
    }

    public void setImgBig(String imgBig) {
        this.imgBig = imgBig;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getImg() {
        return img;
    }

    public void setImg(ArrayList<String> img) {
        this.img = img;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
