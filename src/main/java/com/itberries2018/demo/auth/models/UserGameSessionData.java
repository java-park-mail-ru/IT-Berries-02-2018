package com.itberries2018.demo.auth.models;

import org.springframework.web.socket.WebSocketSession;

public class UserGameSessionData {
    public ProfileData getProfileData() {
        return profileData;
    }

    public void setProfileData(ProfileData profileData) {
        this.profileData = profileData;
    }

    public WebSocketSession getWebSocketSession() {
        return webSocketSession;
    }

    public void setWebSocketSession(WebSocketSession webSocketSession) {
        this.webSocketSession = webSocketSession;
    }

    private ProfileData profileData;
    private WebSocketSession webSocketSession;

    public UserGameSessionData(ProfileData profileData, WebSocketSession webSocketSession) {

        this.profileData = profileData;
        this.webSocketSession = webSocketSession;

    }
}
