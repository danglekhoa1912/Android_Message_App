package com.example.message_app.model;

public class userList {
    private String id,chat;
    private Long time;

    public userList(String id, String chat, Long time) {
        this.id = id;
        this.chat = chat;
        this.time = time;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChat() {
        return chat;
    }

    public void setChat(String chat) {
        this.chat = chat;
    }
}
