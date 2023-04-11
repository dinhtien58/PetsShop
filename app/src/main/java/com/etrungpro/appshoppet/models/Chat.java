package com.etrungpro.appshoppet.models;


import com.google.firebase.Timestamp;

public class Chat {
    String conversationId;
    Timestamp createAt;
    boolean seen;
    String senderId;
    String text ;

    public Chat() {
    }

    public Chat(String conversationId, Timestamp createAt, boolean seen, String senderId, String text) {
        this.conversationId = conversationId;
        this.createAt = createAt;
        this.seen = seen;
        this.senderId = senderId;
        this.text = text;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public Timestamp getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Timestamp createAt) {
        this.createAt = createAt;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
