package com.example.message_app.model;

public class Chat {
    private String imageUrl,mess,receiver,sender,timestamp,status;

    public  Chat(){}
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getMess() {
        return mess;
    }

    public void setMess(String mess) {
        this.mess = mess;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Chat(String imageUrl, String mess, String receiver, String sender, String timestamp, String status) {
        this.imageUrl = imageUrl;
        this.mess = mess;
        this.receiver = receiver;
        this.sender = sender;
        this.timestamp = timestamp;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
