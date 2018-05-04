package com.itberries2018.demo.mechanics.events.service;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.itberries2018.demo.websockets.Message;

public class Connect extends Message {

    private String username;

    @JsonCreator
    public Connect(@JsonProperty("payload") String payload) {
        this.username = payload;
    }

    public String getPayLoad() {
        return username;
    }

    public void setPayload(String payload) {
        this.username = payload;
    }
}
