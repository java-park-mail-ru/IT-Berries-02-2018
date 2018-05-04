package com.itberries2018.demo.mechanics.events.logic;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.itberries2018.demo.mechanics.base.Coordinates;
import com.itberries2018.demo.websockets.Message;

public class Move extends Message {

    private final Payload payload; // реально значащие данные

    public Payload getPayload() {
        return payload;
    }

    @JsonCreator
    public Move(@JsonProperty("payload") Payload payload) {
        this.payload = payload;
    }

    public Coordinates getTo() {
        return payload.to;
    }

    public Coordinates getFrom() {
        return payload.from;
    }


    public static class Payload {
        private final Coordinates from;
        private final Coordinates to;

        public Coordinates getFrom() {
            return from;
        }

        public Coordinates getTo() {
            return to;
        }

        public Payload(@JsonProperty("from") Coordinates from, @JsonProperty("to") Coordinates to) {
            this.from = from;
            this.to = to;
        }
    }
}
