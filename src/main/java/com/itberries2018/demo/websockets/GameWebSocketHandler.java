package com.itberries2018.demo.websockets;

import com.itberries2018.demo.models.ProfileData;
import com.itberries2018.demo.servicesintefaces.UserService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import static org.springframework.web.socket.CloseStatus.SERVER_ERROR;

@Component
public class GameWebSocketHandler extends TextWebSocketHandler {

    private static final CloseStatus ACCESS_DENIED = new CloseStatus(4500, "Not logged in. Access denied");

    @NotNull
    private final RemotePointService remotePointService;
    @NotNull
    private final UserService userService;

    @Autowired
    public GameWebSocketHandler(@NotNull RemotePointService remotePointService,
                                @NotNull UserService userService) {
        this.remotePointService = remotePointService;
        this.userService = userService;
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String reply = message.getPayload();
        final Long id = (Long) session.getAttributes().get("userId");
        if (this.userService.findById(id) != null) {
            ProfileData profileData =  new ProfileData(this.userService.findById(id));
            if (reply.equals("Alians")) {
                this.remotePointService.addAlian(profileData, session);
                session.sendMessage(new TextMessage("session added to aliens queue"));
            } else if (reply.equals("Humans")) {
                this.remotePointService.addHuman(profileData, session);
                session.sendMessage(new TextMessage("session added to humans queue"));
            } else {
                session.sendMessage(new TextMessage("wrong fraction"));
            }
        } else {
            closeSessionSilently(session, ACCESS_DENIED);
            return;
        }
    }

    private void closeSessionSilently(@NotNull WebSocketSession session, @Nullable CloseStatus closeStatus) {
        final CloseStatus status = closeStatus == null ? SERVER_ERROR : closeStatus;
        //noinspection OverlyBroadCatchBlock
        try {
            session.close(status);
        } catch (Exception ignore) {
            return;
        }

    }

}
