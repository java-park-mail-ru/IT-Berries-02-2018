package com.itberries2018.demo.websockets;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.itberries2018.demo.mechanics.events.game.GameResult;
import com.itberries2018.demo.mechanics.events.game.Start;
import com.itberries2018.demo.mechanics.events.game.Turn;
import com.itberries2018.demo.mechanics.events.logic.Move;
import com.itberries2018.demo.mechanics.events.logic.Score;
import com.itberries2018.demo.mechanics.events.service.Connect;
import com.itberries2018.demo.mechanics.events.service.Ping;
import com.itberries2018.demo.mechanics.messages.JoinGame;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "event")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Connect.class, name = "EVENTS.SERVICE.CONNECT"),
        @JsonSubTypes.Type(value = Ping.class, name = "EVENTS.SERVICE.PING"),

        @JsonSubTypes.Type(value = Start.class, name = "EVENTS.GAME.START"),
        @JsonSubTypes.Type(value = GameResult.class, name = "EVENTS.GAME.RESULT"),
        @JsonSubTypes.Type(value = Turn.class, name = "EVENTS.GAME.TURN"),

        @JsonSubTypes.Type(value = Score.class, name = "EVENTS.LOGIC.SCORE"),

        @JsonSubTypes.Type(value = Move.class, name = "EVENTS.LOGIC.MOVE"),

        @JsonSubTypes.Type(value = JoinGame.class, name = "MESSAGES.JOINGAME"),

})

public abstract class Message {

}
