package com.itberries2018.demo.auth.models;

import com.itberries2018.demo.auth.entities.History;
import com.itberries2018.demo.auth.entities.User;

public class ScoreRecord {

    private long id;
    private long score;
    private String username;

    public ScoreRecord(User user, History history) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.score = history.getScore();

    }

    public ScoreRecord(Long score, String username) {
        this.score = score;
        this.username = username;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }
}
