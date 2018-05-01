package com.itberries2018.demo.websockets;

import com.itberries2018.demo.models.ProfileData;
import com.itberries2018.demo.models.UserGameSessionData;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayDeque;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RemotePointService {

    private final ArrayDeque<UserGameSessionData> Alians = new ArrayDeque<UserGameSessionData>();
    private final ArrayDeque<UserGameSessionData> Humans = new ArrayDeque<UserGameSessionData>();

    public void  addAlian(ProfileData profileData, WebSocketSession session) {
        Alians.addLast(new UserGameSessionData(profileData, session));
    }

    public void addHuman(ProfileData profileData, WebSocketSession session) {
        Humans.addLast(new UserGameSessionData(profileData, session));
    }

}
