package com.etrungpro.appshoppet.models;

import com.google.firebase.Timestamp;

public class User {
    String email;
    String firstName;
    String lastName;
    String img;
    String hashPassword;
    Timestamp createAt;

    public User() {

    }

    public User(String email, String firstName, String lastName, String img, String hashPassword, Timestamp createAt) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.img = img;
        this.hashPassword = hashPassword;
        this.createAt = createAt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getHashPassword() {
        return hashPassword;
    }

    public void setHashPassword(String hashPassword) {
        this.hashPassword = hashPassword;
    }

    public Timestamp getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Timestamp createAt) {
        this.createAt = createAt;
    }
}
