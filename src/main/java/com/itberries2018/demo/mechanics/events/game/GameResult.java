package com.itberries2018.demo.mechanics.events.game;

import com.itberries2018.demo.mechanics.player.GamePlayer;
import com.itberries2018.demo.websockets.Message;

public class GameResult extends Message {

    private final Payload payload;

    public Payload getPayload() {
        return payload;
    }

    public GameResult(GamePlayer winner, GamePlayer loser, String reason) {
        this.payload = new Payload(winner, loser, reason);
    }

    public static class Payload {
        private final GamePlayer winner;
        private final GamePlayer loser;
        private final String reason;

        public Payload(GamePlayer winner, GamePlayer loser, String reason) {
            this.winner = winner;
            this.loser = loser;
            this.reason = reason;
        }

        public GamePlayer getWinner() {
            return winner;
        }

        public GamePlayer getLoser() {
            return loser;
        }

        public String getReason() {
            return reason;
        }
    }
}
