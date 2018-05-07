package com.itberries2018.demo.mechanics.events.game;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.itberries2018.demo.mechanics.game.GameSession;
import com.itberries2018.demo.mechanics.game.MapCell;
import com.itberries2018.demo.websockets.Message;

public class Start extends Message {

    private final Payload payload;

    public Payload getPayload() {
        return payload;
    }

    public Start(GameSession newGame) {
        this.payload = new Payload(newGame.getMap().getCells().clone());
    }

    public static class Payload {
        private final MapCell[][] cells;

        @JsonCreator
        public Payload(MapCell[][] cells) {
            this.cells = cells.clone();
        }

        public MapCell[][] getCells() {
            return cells;
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
