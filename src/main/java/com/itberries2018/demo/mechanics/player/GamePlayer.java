package com.itberries2018.demo.mechanics.player;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.itberries2018.demo.auth.entities.User;
import net.minidev.json.annotate.JsonIgnore;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class GamePlayer {
    @JsonIgnore
    private final Long id;
    private final String name;

    public GamePlayer(Long id, String name, String sideOfTheBattle) {
        this.id = id;
        this.name = name;
    }

    public GamePlayer(User user) {
        this.id = user.getId();
        this.name = user.getUsername();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
