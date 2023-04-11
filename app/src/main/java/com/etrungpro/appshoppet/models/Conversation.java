package com.etrungpro.appshoppet.models;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;

import java.util.ArrayList;

public class Conversation {
    ArrayList<String> users;
    Timestamp createAt;
    String lastMessage;

    public Conversation() {
    }

    public Conversation(ArrayList<String> users, Timestamp createAt, String lastMessage) {
        this.users = users;
        this.createAt = createAt;
        this.lastMessage = lastMessage;
    }

    public ArrayList<String> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<String> users) {
        this.users = users;
    }

    public Timestamp getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Timestamp createAt) {
        this.createAt = createAt;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }
}
