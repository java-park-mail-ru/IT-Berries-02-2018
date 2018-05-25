package com.itberries2018.demo.mechanics.game;

import com.itberries2018.demo.websockets.RemotePointService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;

public class TurnTimer implements Runnable  {

    private GameSession session;
    private long turns;
    private GameSession.Turn turn;

    @NotNull
    private final RemotePointService service;

    @Autowired
    public TurnTimer(GameSession session, long turns, GameSession.Turn turn, RemotePointService service) {
        this.session = session;
        this.turns = turns;
        this.turn = turn;
        this.service = service;
    }

    @Override
    public void run() {
       /* long time = System.currentTimeMillis();
        for (GameSession session : this.service.getGames()) {
            if (session.getStatus() == GameSession.Status.IN_GAME && (time - session.getTurnTimer()) >= 30000L) {
                if (session.getTurn() == GameSession.Turn.HUMAN) {
                    try {
                        service.sendMessageToUser(session.getHuman().getId(), new Turn("ufo"));
                        service.sendMessageToUser(session.getUfo().getId(), new Turn("ufo"));
                    } catch (Exception e) {
                        continue;
                    }
                } else {
                    try {
                        service.sendMessageToUser(session.getUfo().getId(), new Turn("human"));
                        service.sendMessageToUser(session.getHuman().getId(), new Turn("human"));
                    } catch (Exception e) {
                        continue;
                    }
                }
                session.choseTurn();
            }
        }*/
    }
}
