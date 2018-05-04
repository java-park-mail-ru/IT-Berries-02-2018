package com.itberries2018.demo.mechanics.player;

import com.fasterxml.jackson.annotation.JsonInclude;
import net.minidev.json.annotate.JsonIgnore;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class GamePlayer {
    @JsonIgnore
    private final Long id;
    private final String name;
    private final String sideOfTheBattle;

    public GamePlayer(Long id, String name, String sideOfTheBattle) {
        this.id = id;
        this.name = name;
        this.sideOfTheBattle = sideOfTheBattle;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSideOfTheBattle() {
        return sideOfTheBattle;
    }
}
