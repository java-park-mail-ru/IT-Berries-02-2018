package com.itberries2018.demo.mechanics;

import com.itberries2018.demo.mechanics.services.GameSessionsService;
import com.itberries2018.demo.websockets.RemotePointService;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

@Service
public class GameMechanicImpl implements GameMechanics {

    @NotNull
    private final GameSessionsService gameSessionService;

    @NotNull
    private final RemotePointService remotePointService;

    public GameMechanicImpl(@NotNull RemotePointService remotePointService,
                            @NotNull GameSessionsService gameSessionsService) {
        this.remotePointService = remotePointService;
        this.gameSessionService = gameSessionsService;
    }

    @Override
    public void gamesStep() {
        this.gameSessionService.checkForNewSessions();

    }

    @Override
    public void reset() {

    }
}
