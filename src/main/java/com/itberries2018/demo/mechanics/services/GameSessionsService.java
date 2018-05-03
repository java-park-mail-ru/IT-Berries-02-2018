package com.itberries2018.demo.mechanics.services;

import com.itberries2018.demo.game.GameSession;
import com.itberries2018.demo.websockets.RemotePointService;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.Set;

@Service
public class GameSessionsService {

    private final Set<GameSession> gameSessions = new LinkedHashSet<>();

    @NotNull
    private final RemotePointService remotePointService;

    public GameSessionsService(@NotNull RemotePointService remotePointService) {
        this.remotePointService = remotePointService;
    }

    public void checkForNewSessions() {
        for (GameSession session : this.remotePointService.getNewSessions()) {
            if (session.getStatus() == GameSession.Status.CREATED) {
                gameSessions.add(session);
            }
        }
        for (GameSession session : this.gameSessions) {
            switch (session.getStatus()) {
                case READY_FOR_START:
                    session.start();
                    break;
                case OVERED:
                    gameSessions.remove(session);
                    break;
                default:
                    session.step();
            }
        }
    }
}
