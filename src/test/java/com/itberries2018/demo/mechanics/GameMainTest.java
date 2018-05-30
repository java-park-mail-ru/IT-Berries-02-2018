package com.itberries2018.demo.mechanics;

import com.itberries2018.demo.auth.entities.User;
import com.itberries2018.demo.mechanics.base.Coordinates;
import com.itberries2018.demo.mechanics.events.logic.Move;
import com.itberries2018.demo.mechanics.game.GameMap;
import com.itberries2018.demo.mechanics.game.GameSession;
import com.itberries2018.demo.mechanics.player.GamePlayer;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;

public class GameMainTest {
    public final List<GamePlayer> playerList;

    public GameMainTest() {
        playerList = new ArrayList<GamePlayer>();
        User human = new User("testHuman", "testHuman@mail.ru", "passwordtestHuman");
        User ufo = new User("testUfo", "testUfo@mail.ru", "passwotftestUfo");
        ufo.setId(1);
        human.setId(2);
        playerList.add(new GamePlayer(human));
        playerList.add(new GamePlayer(ufo));
    }

    @Test
    public void createFieldTest() {
        final GameSession gameSession = new GameSession(playerList.get(0), playerList.get(1));
        GameMap result = gameSession.getMap();
        int amountOfShips = 20, width = 0, height = 0;
        assertThat(amountOfShips, is(result.getBaseRocketValue()));
        assertNotEquals(width, is(result.getWidth()));
        assertNotEquals(height, result.getHeight());
    }

    @Test
    public void successfulMoveTest() {
        final GameSession gameSession = new GameSession(playerList.get(0), playerList.get(1));
        Coordinates from = new Coordinates(0, 0);
        Coordinates to = new Coordinates(2, 2);
        Move move = new Move(new Move.Payload(from, to));
        boolean result = gameSession.step(move);
        boolean expected = true;
        assertThat(expected, is(result));
    }

    @Test
    public void unSuccessfulMoveTest() {
        final GameSession gameSession = new GameSession(playerList.get(0), playerList.get(1));
        boolean result = gameSession.step(new Move(new Move.Payload(new Coordinates(0, 0), new Coordinates(200, 200))));
        boolean expected = false;
        assertThat(expected, is(result));
    }

    @Test
    public void negativeMoveIsProhibited() {
        final GameSession gameSession = new GameSession(playerList.get(0), playerList.get(1));
        boolean result = gameSession.step(new Move(new Move.Payload(new Coordinates(0, 0), new Coordinates(-1, 2))));
        boolean expected = false;
        assertThat(expected, is(result));
    }
}
