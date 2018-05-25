package com.itberries2018.demo.mechanics.events.game;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.itberries2018.demo.websockets.Message;

public class Turn extends Message {
    private final Payload payload;

    public Payload getPayload() {
        return payload;
    }

    @JsonCreator
    public Turn(@JsonProperty("payload") Payload payload) {
        this.payload = payload;
    }

    public Turn(String turn) {
        this.payload = new Payload(turn);
    }

    public static class Payload {
        private final String turn;

        @JsonCreator
        public Payload(String turn) {
            this.turn = turn;
        }

        public String getTurn() {
            return turn;
        }
    }
}
