package com.itberries2018.demo.mechanics;

import com.itberries2018.demo.entities.User;
import com.itberries2018.demo.mechanics.services.GameSessionsService;
import com.itberries2018.demo.models.ProfileData;
import com.itberries2018.demo.models.UserGameSessionData;
import com.itberries2018.demo.servicesintefaces.UserService;
import com.itberries2018.demo.websockets.RemotePointService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketExtension;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ExtendWith(SpringExtension.class)
class GamesTest {

    @MockBean
    private RemotePointService remotePointService;

    @MockBean
    private GameSessionsService gameSessionService;

    @MockBean
    private UserService userService;


    @Test
    void setUp() {
        ProfileData user1 = new ProfileData(new User("username", "user@mail.ru", "password", "avatar.png"));
        ProfileData user2 = new ProfileData(new User("username2", "user2@mail.ru", "password2", "avatar2.png"));
        WebSocketSession webSocketSession = new WebSocketSession() {
            @Override
            public String getId() {
                return null;
            }

            @Override
            public URI getUri() {
                return null;
            }

            @Override
            public HttpHeaders getHandshakeHeaders() {
                return null;
            }

            @Override
            public Map<String, Object> getAttributes() {
                return null;
            }

            @Override
            public Principal getPrincipal() {
                return null;
            }

            @Override
            public InetSocketAddress getLocalAddress() {
                return null;
            }

            @Override
            public InetSocketAddress getRemoteAddress() {
                return null;
            }

            @Override
            public String getAcceptedProtocol() {
                return null;
            }

            @Override
            public void setTextMessageSizeLimit(int i) {

            }

            @Override
            public int getTextMessageSizeLimit() {
                return 0;
            }

            @Override
            public void setBinaryMessageSizeLimit(int i) {

            }

            @Override
            public int getBinaryMessageSizeLimit() {
                return 0;
            }

            @Override
            public List<WebSocketExtension> getExtensions() {
                return null;
            }

            @Override
            public void sendMessage(WebSocketMessage<?> webSocketMessage) throws IOException {

            }

            @Override
            public boolean isOpen() {
                return false;
            }

            @Override
            public void close() throws IOException {

            }

            @Override
            public void close(CloseStatus closeStatus) throws IOException {

            }
        };
        UserGameSessionData userData1 = new UserGameSessionData(user1, webSocketSession);
        UserGameSessionData userData2 = new UserGameSessionData(user2, webSocketSession);
        //GameSession gameSession = new GameSession(userData1, userData2);
        //ArrayList<MapCell> rockets = new ArrayList<MapCell>();
        //GameMap map = gameSession.getMap();
        //rockets = map.getRockets();
    }
}