package com.itberries2018.demo.mechanics.events.logic;

import com.itberries2018.demo.websockets.Message;

public class Score extends Message {

    private final Payload payload;

    public Payload getPayload() {
        return payload;
    }

    public static class Payload {

        private final long score;
        private final String name;

        public Payload(long score, String name) {
            this.score = score;
            this.name = name;
        }

        public long getScore() {
            return this.score;
        }

        public String getName() {
            return name;
        }
    }

    public Score(long score, String name) {
        this.payload = new Payload(score, name);
    }

}
