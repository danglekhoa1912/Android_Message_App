package com.example.message_app.model;

public class Message {


    private String emoji;
    private String image;
    private String chatMess;

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    private String senderId;

    public Message(String emoji, String image, String chatMess, String senderId) {
        this.emoji = emoji;
        this.image = image;
        this.chatMess = chatMess;
        this.senderId = senderId;
    }

    public String getEmoji() {
        return emoji;
    }

    public void setEmoji(String emoji) {
        this.emoji = emoji;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getChatMess() {
        return chatMess;
    }

    public void setChatMess(String chatMess) {
        this.chatMess = chatMess;
    }
}
