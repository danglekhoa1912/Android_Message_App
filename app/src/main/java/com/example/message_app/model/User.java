package com.example.message_app.model;

import java.util.List;

public class User {


    private String userName,avatar,mobile;
    private List<String> listFriend,blockList;

    public User(String userName, String avatar, String mobile, List<String> listFriend, List<String> blockList) {
        this.userName = userName;
        this.avatar = avatar;
        this.mobile = mobile;
        this.listFriend = listFriend;
        this.blockList = blockList;
    }

    public  User(){}


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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

}
