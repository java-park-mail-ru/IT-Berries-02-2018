package com.itberries2018.demo.mechanics.events.game;

import com.itberries2018.demo.mechanics.Game;
import com.itberries2018.demo.websockets.Message;

public class Start extends Message {
    public Start(Game newGame) {

    }

//    public final Payload payload;
//    public Start(Game game){
//        this.payload = new Payload(game);
//    }
//    public static class Payload{
//        public final int sizeX;
//        public final int sizeY;
//        //Список с ракетами
//
//
//        public Payload(Game game) {
//            this.sizeX = game.getSizeX();
//            this.sizeY = game.getSizeY();
//            //создание списка с ракетами
//
//            //Проход по списку и размещение и ракет
//        }
//
//    }
}
