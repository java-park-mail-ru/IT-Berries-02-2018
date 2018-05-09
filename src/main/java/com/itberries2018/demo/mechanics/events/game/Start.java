package com.itberries2018.demo.mechanics.events.game;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.itberries2018.demo.mechanics.game.GameSession;
import com.itberries2018.demo.mechanics.game.MapCell;
import com.itberries2018.demo.mechanics.player.GamePlayer;
import com.itberries2018.demo.websockets.Message;

public class Start extends Message {

    private final Payload payload;

    public Payload getPayload() {
        return payload;
    }

    public Start(GameSession newGame) {
        this.payload = new Payload(newGame);
    }

    public static class Payload {
        private final MapCell[][] cells;
        private final GamePlayer humansPlayer;
        private final GamePlayer ufoPlayer;

        @JsonCreator
        public Payload(GameSession newGame) {
            this.cells = newGame.getMap().getCells().clone();
            this.humansPlayer = newGame.getHuman();
            this.ufoPlayer = newGame.getUfo();
        }

        public MapCell[][] getCells() {
            return cells;
        }

        public GamePlayer getHumansPlayer() {
            return humansPlayer;
        }

        public GamePlayer getUfoPlayer() {
            return ufoPlayer;
        }
    }

    /* public final Payload payload;
    public Start(Game game){
        this.payload = new Payload(game);
    }
    public static class Payload{
       public final int sizeX;
       public final int sizeY;
        //Список с ракетами


        public Payload(Game game) {
            this.sizeX = game.getSizeX();
           this.sizeY = game.getSizeY();
           //создание списка с ракетами

           //Проход по списку и размещение и ракет
        }
    }*/
}
