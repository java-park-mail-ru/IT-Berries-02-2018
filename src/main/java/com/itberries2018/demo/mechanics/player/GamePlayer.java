package com.itberries2018.demo.mechanics.player;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.itberries2018.demo.auth.entities.User;
import net.minidev.json.annotate.JsonIgnore;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class GamePlayer {
    @JsonIgnore
    private final Long id;
    private final String name;
    private int score;

    public int getTurns() {
        return turns;
    }

    public void setTurns(int turns) {
        this.turns = turns;
    }

    private int turns;

    public GamePlayer(User user) {
        this.id = user.getId();
        this.name = user.getUsername();
        this.score = 0;
        this.turns = 0;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
