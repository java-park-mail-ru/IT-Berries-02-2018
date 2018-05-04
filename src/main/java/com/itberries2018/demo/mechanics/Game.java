package com.itberries2018.demo.mechanics;

import com.itberries2018.demo.mechanics.events.game.Turn;
import com.itberries2018.demo.mechanics.player.GamePlayer;
import com.itberries2018.demo.websockets.Message;

import java.util.ArrayList;
import java.util.List;

//Прототип !!!
public class Game {
    private final List<GamePlayer> playerList;
    private int sizeX;
    private int sizeY;

    public List<GamePlayer> getPlayerList() {
        return playerList;
    }

    public long getLatestTurnStart() {

        return latestTurnStart;
    }

    //private final Cell[][] map;
    private long latestTurnStart;
    private int currentPlayerId;

    public Game(List<GamePlayer> playerList, int sizeX, int sizeY, long latestTurnStart, int currentPlayerId) {
        this.playerList = playerList;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.latestTurnStart = latestTurnStart;
        this.currentPlayerId = currentPlayerId;
    }

    public Game(List<GamePlayer> playerList) {
        this.playerList = playerList;
    }

    public synchronized List<Message> finishTurn() {
        final List<Message> newTurnMessages = new ArrayList<>();
        newTurnMessages.add(new Turn(new Turn.Payload()));

        // ......................

        return newTurnMessages;
    }
}
