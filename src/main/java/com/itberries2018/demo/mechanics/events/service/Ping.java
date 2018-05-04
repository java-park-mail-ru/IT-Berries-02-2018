package com.itberries2018.demo.mechanics.events.service;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Ping {

    private String username;
    private String event;

    @JsonCreator
    public Ping(@JsonProperty("payload") String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
