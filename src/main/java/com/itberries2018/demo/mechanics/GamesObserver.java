package com.itberries2018.demo.mechanics;


import org.jetbrains.annotations.NotNull;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.Clock;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import org.slf4j.Logger;

@Service
public class GamesObserver implements Runnable  {

    private static final long STEP_TIME = 50;

    @NotNull
    private final GameMechanics gameMechanics;

    @NotNull
    private final Clock clock = Clock.systemDefaultZone();

    @NotNull
    private static final Logger LOGGER = LoggerFactory.getLogger(GamesObserver.class);

    private final Executor tickExecutor = Executors.newSingleThreadExecutor();

    @Autowired
    public GamesObserver(@NotNull GameMechanics gameMechanics) {
        this.gameMechanics = gameMechanics;
    }

    @PostConstruct
    public void initAfterStartup() {
        tickExecutor.execute(this);
    }

    @Override
    public void run() {
        try {
            this.mainCycle();
        } finally {
            LOGGER.warn("Mechanic executor terminated");
        }
    }

    private void mainCycle() {
        while (true) {
            try {
                final long before = clock.millis();
                gameMechanics.gamesStep();
                final long after = clock.millis();
                try {
                    final long sleepingTime = Math.max(0, STEP_TIME - (after - before));
                    Thread.sleep(sleepingTime);
                } catch (InterruptedException e) {
                    LOGGER.error("Mechanics thread was interrupted", e);
                }
            } catch (RuntimeException e) {
                LOGGER.error("Mechanics executor was reseted due to exception", e);
                gameMechanics.reset();
            }
        }
    }
}
