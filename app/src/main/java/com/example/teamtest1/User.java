package com.example.teamtest1;

public class User {
    private String photoUrl;
    private String id;
    private int pw;
    private String nickName;

    public User(){}

    public User(String photoUrl, String id, String nickName) {
        this.photoUrl = photoUrl;
        this.id = id;
//        this.pw = pw;
        this.nickName = nickName;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getPw() {
        return pw;
    }

    public void setPw(int pw) {
        this.pw = pw;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
