package com.example.message_app.model;

public class User {


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String[] getListFriend() {
        return listFriend;
    }

    public void setListFriend(String[] listFriend) {
        this.listFriend = listFriend;
    }

    public String[] getBlockList() {
        return blockList;
    }

    public void setBlockList(String[] blockList) {
        this.blockList = blockList;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    private String id,userName,password,name,avatar;
    private String[] listFriend,blockList;
    private boolean status;
    private Chat[] listChat;

    public Chat[] getListChat() {
        return listChat;
    }

    public void setListChat(Chat[] listChat) {
        this.listChat = listChat;
    }

    public User(String id, String userName, String password, String name, String avatar, String[] listFriend, String[] blockList, boolean status, Chat[] listChat) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.name = name;
        this.avatar = avatar;
        this.listFriend = listFriend;
        this.blockList = blockList;
        this.status = status;
        this.listChat = listChat;
    }
}
