package com.itberries2018.demo.mechanics.messages;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.itberries2018.demo.websockets.Message;

public class JoinGame extends Message {

    private String side;

    @JsonCreator
    public JoinGame(@JsonProperty("payload") String side) {
        this.side = side;
    }

    public String getPayLoad() {
        return side;
    }

    public void setPayload(String payload) {
        this.side = payload;
    }
}
