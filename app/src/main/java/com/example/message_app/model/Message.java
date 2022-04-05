package com.example.message_app.model;

public class Message {



    private String chatMess;
    private String senderId;

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }


    public Message( String chatMess, String senderId) {
        this.chatMess = chatMess;
        this.senderId = senderId;
    }


    public String getChatMess() {
        return chatMess;
    }

    public void setChatMess(String chatMess) {
        this.chatMess = chatMess;
    }
}
