package com.example.message_app.model;

import java.util.List;

public class User {

    private String userName,avatar;
    private List<String> listFriend,blockList;
    private List<Chat> listChat;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public List<String> getListFriend() {
        return listFriend;
    }

    public void setListFriend(List<String> listFriend) {
        this.listFriend = listFriend;
    }

    public List<String> getBlockList() {
        return blockList;
    }

    public void setBlockList(List<String> blockList) {
        this.blockList = blockList;
    }

    public List<Chat> getListChat() {
        return listChat;
    }

    public void setListChat(List<Chat> listChat) {
        this.listChat = listChat;
    }

    public User(String userName, String avatar, List<String> listFriend, List<String> blockList, List<Chat> listChat) {
        this.userName = userName;
        this.avatar = avatar;
        this.listFriend = listFriend;
        this.blockList = blockList;
        this.listChat = listChat;
    }
}
