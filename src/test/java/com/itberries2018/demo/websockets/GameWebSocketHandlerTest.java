package com.itberries2018.demo.websockets;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itberries2018.demo.mechanics.base.Coordinates;
import com.itberries2018.demo.mechanics.events.game.Turn;
import com.itberries2018.demo.mechanics.events.logic.Move;
import com.itberries2018.demo.mechanics.events.logic.Score;
import com.itberries2018.demo.mechanics.events.service.Connect;
import com.itberries2018.demo.mechanics.messages.JoinGame;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class GameWebSocketHandlerTest {

    final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testMove() throws IOException {
        final Message message = objectMapper.readValue("{\"event\":\"EVENTS.LOGIC.MOVE\",\"payload\":{\"from\":{\"xval\":5,\"yval\":5},\"to\":{\"xval\":6,\"yval\":5}}}", Message.class);
        assertThat(message.getClass()).isEqualTo(Move.class);
    }

    @Test
    public void testTurn() throws IOException {
        final Message message = objectMapper.readValue("{\"event\":\"EVENTS.GAME.TURN\",\"payload\":\"ufo\"}", Message.class);
        assertThat(message.getClass()).isEqualTo(Turn.class);
    }

    @Test
    public void testConnect() throws Exception {
        final Message message = objectMapper.readValue("{\"event\":\"EVENTS.SERVICE.CONNECT\",\"payload\":\"testytest\"}",
                Message.class);
        assertThat(message.getClass()).isEqualTo(Connect.class);
    }


    @Test
    public void joinGameTest() throws IOException {
        final Message message = objectMapper.readValue("{\"event\":\"MESSAGES.JOINGAME\",\"payload\":\"humans\"}",
                Message.class);
        assertThat(message.getClass()).isEqualTo(JoinGame.class);
    }

}