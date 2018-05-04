package com.itberries2018.demo.auth.models;

import com.itberries2018.demo.auth.entities.User;

public class ProfileData {
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }

    private String username;
    private String email;
    private String avatar;
    private long score;

    public ProfileData(User user) {
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.avatar = user.getAvatar();
    }
}
