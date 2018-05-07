package com.itberries2018.demo.websockets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itberries2018.demo.auth.entities.User;
import com.itberries2018.demo.mechanics.Game;
import com.itberries2018.demo.mechanics.events.game.GameResult;
import com.itberries2018.demo.mechanics.events.game.Start;
import com.itberries2018.demo.mechanics.events.game.Turn;
import com.itberries2018.demo.mechanics.game.GameSession;
import com.itberries2018.demo.mechanics.messages.JoinGame;
import com.itberries2018.demo.mechanics.player.GamePlayer;
import com.itberries2018.demo.auth.servicesintefaces.ScoreRecordService;
import com.itberries2018.demo.auth.servicesintefaces.UserService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

import static java.util.Collections.singletonMap;

@SuppressWarnings("OverlyBroadThrowsClause")
@Service
public class RemotePointService {
    @NotNull
    private final UserService userService;
    private final ScoreRecordService scoreRecordService;
    private final Map<Long, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper;
    private final Logger logger = LoggerFactory.getLogger(RemotePointService.class);

    private final Queue<Long> humans = new ConcurrentLinkedDeque<>();
    private final Queue<Long> aliens = new ConcurrentLinkedDeque<>();
    private final Queue<Long> waiters = new ConcurrentLinkedDeque<>();
    private final List<GameSession> games = new ArrayList<>();
    private final Map<Long, GameSession> gameMap = new ConcurrentHashMap<>();
    public static final long TURN_DURATION_MILLS = 30 * 1000;

    public RemotePointService(@NotNull UserService userService,
                              @NotNull ScoreRecordService scoreRecordService,
                              @NotNull ObjectMapper objectMapper) {
        this.userService = userService;
        this.scoreRecordService = scoreRecordService;
        this.objectMapper = objectMapper;
    }

    /*private ScheduledExecutorService service = Executors.newScheduledThreadPool(1);*/
    /*private ScheduledFuture future = service.scheduleAtFixedRate(new GameDispatcher(), 0, 1, TimeUnit.SECONDS);*/

    /*private class GameDispatcher implements Runnable {

        @Override
        public void run() {
            for (GameSession game : games) {
                if (game.getLatestTurnStart() + TURN_DURATION_MILLS < System.currentTimeMillis()) {
                    try {
                        sendGameMessages(game.finishTurn(), game);
                    } catch (Exception ex) {
                        logger.warn("ERROR SENDING MESSAGE FROM GAMEDISPATCHER");
                    }
                }
            }
        }

    }*/

    public void handleGameMessage(Message message, Long userID) throws IOException {
        final GameSession game = gameMap.get(userID);
        // sendGameMessages(game.interact(message, userID), game); возможно, для взаимодейвтсвия нло и ракеты
    }

    public void sendGameMessages(@Nullable List<Message> messages, Game game) throws Exception {
        for (Message message : messages) {
            for (GamePlayer player : game.getPlayerList()) {
                if (message.getClass() == GameResult.class) {
                    finishGame(game, (GameResult) message);
                    return;
                }
                if (isConnected(player.getId())) {
                    sendMessageToUser(player.getId(), message);
                }
            }
        }
    }

    public boolean isConnected(Long userId) {
        return sessions.containsKey(userId) && sessions.get(userId).isOpen();
    }

    public void sendMessageToUser(Long userId, @NotNull Message message) throws IOException {
        final WebSocketSession webSocketSession = sessions.get(userId);
        if (webSocketSession == null) {
            throw new IOException("No game websocket for user " + userId);
        }
        if (!webSocketSession.isOpen()) {
            throw new IOException("Session is closed or not exsists ");
        }
        try {
            webSocketSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
        } catch (Exception ex) {
            throw new IOException("Unable to send message", ex);
        }
    }

    public void finishGame(Game game, GameResult gameResult) throws Exception {
        final Long winnerId = gameResult.getPayload().getWinner().getId();
        final Long loserId = gameResult.getPayload().getLoser().getId();
        scoreRecordService.incrementScore(winnerId);
        sendMessageToUser(winnerId, gameResult);
        sendMessageToUser(loserId, gameResult);
        sessions.get(winnerId).close();
        sessions.get(loserId).close();
        sessions.remove(winnerId);
        sessions.remove(loserId);
        games.remove(game);
        gameMap.remove(loserId);
        gameMap.remove(winnerId);
    }

    public void addWaiter(JoinGame joinGame, long userId) throws IOException {
        waiters.add(userId);
        if (joinGame.getPayLoad().equals("humans")) {
            humans.add(userId);
        } else if (joinGame.getPayLoad().equals("aliens")) {
            aliens.add(userId);
        } else {
            logger.error("ERROR INCORRECT SIDE");
        }
        if (humans.size() > 0 && aliens.size() > 0) {
            final Long humansUserId = humans.poll();
            final Long aliensUserId = aliens.poll();
            waiters.remove(humansUserId);
            waiters.remove(aliensUserId);
            final TextMessage message = new TextMessage(
                objectMapper.writeValueAsString(
                    singletonMap("message", "Game created, connecting to game")
                )
            );
            sessions.get(humansUserId).sendMessage(message);
            sessions.get(aliensUserId).sendMessage(message);

            final User humansUser = userService.findById(humansUserId);
            final User aliensUser = userService.findById(aliensUserId);

            final GameSession gameSession = new GameSession(new GamePlayer(aliensUser), new GamePlayer(humansUser));
            sendMessageToUser(humansUserId, new Start(gameSession));
            sendMessageToUser(aliensUserId, new Start(gameSession));
            games.add(gameSession);
            gameMap.put(humansUserId, gameSession);
            gameMap.put(aliensUserId, gameSession);
            sendMessageToUser(humansUserId, new Turn("human"));
            sendMessageToUser(aliensUserId, new Turn("human"));
        } else {
            sessions.get(userId).sendMessage(
                new TextMessage(objectMapper.writeValueAsString(
                    singletonMap("message", "waiting for new users")
                ))
            );
        }
    }

    public void registerUser(Long userId, @NotNull WebSocketSession webSocketSession) throws IOException {

        logger.info("User with " + userId + " connected");
        sessions.put(userId, webSocketSession);

    }

    public void disconnectedHandler(Long userId) {
        final WebSocketSession webSocketSession = sessions.get(userId);
        if (waiters.contains(userId)) {
            waiters.remove(userId);
        } else if (gameMap.containsKey(userId)) {
            final GameSession userGame = gameMap.get(userId);
            for (Map.Entry<Long, GameSession> entry : gameMap.entrySet()) {
                if (Objects.equals(entry.getValue(), userGame)) {
                    final GamePlayer winner;
                    final GamePlayer loser;

                    if (Objects.equals(userGame.getHuman().getId(), userId)) {
                        winner = userGame.getUfo();
                        loser = userGame.getHuman();
                    } else {
                        winner = userGame.getHuman();
                        loser = userGame.getUfo();
                    }
                    try {
                        sendMessageToUser(winner.getId(), new GameResult(
                            winner, loser, "Your opponent disconnected"
                        ));
                    } catch (IOException ignore) {
                        ignore.printStackTrace();
                        logger.error("ERROR CLOSING WEBSOCKET");
                    }
                    scoreRecordService.incrementScore(winner.getId());
                    WebSocketSession secondUserSesion = sessions.get(entry.getKey());
                    this.closeWebScoket(secondUserSesion);
                    gameMap.remove(winner.getId());
                    gameMap.remove(loser.getId());
                    sessions.remove(winner.getId());
                    break;
                }
            }
            games.remove(userGame);
        }
        this.closeWebScoket(webSocketSession);
        sessions.remove(userId);
    }

    public void closeWebScoket(WebSocketSession webSocketSession) {
        if (webSocketSession != null && webSocketSession.isOpen()) {
            try {
                webSocketSession.close();
            } catch (IOException ignore) {
                ignore.printStackTrace();
                logger.error("ERROR CLOSING WEBSOCKET");
            }
        }
    }
}
