package com.itberries2018.demo.models;

public class ScoreRecord  {

    private long id;
    private long score;
    private String username;

    public ScoreRecord(User user) {
        this.id = user.getId();
        this.username = user.getLogin();
        this.score = user.getScore();
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
