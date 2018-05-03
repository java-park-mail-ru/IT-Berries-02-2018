package com.itberries2018.demo.websockets;

import com.itberries2018.demo.game.GameSession;
import com.itberries2018.demo.models.ProfileData;
import com.itberries2018.demo.models.UserGameSessionData;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayDeque;
import java.util.LinkedHashSet;
import java.util.Set;

@Service
public class RemotePointService {

    private final Set<GameSession> gameSessions = new LinkedHashSet<>();
    private final ArrayDeque<UserGameSessionData> alians = new ArrayDeque<UserGameSessionData>();
    private final ArrayDeque<UserGameSessionData> humans = new ArrayDeque<UserGameSessionData>();

    public void  addAlian(ProfileData profileData, WebSocketSession session) {
        alians.addLast(new UserGameSessionData(profileData, session));
    }

    public void addHuman(ProfileData profileData, WebSocketSession session) {
        humans.addLast(new UserGameSessionData(profileData, session));
    }

    public Set<GameSession> getNewSessions() {
        return  gameSessions;
    }
}
