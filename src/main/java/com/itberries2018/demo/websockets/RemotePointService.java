package com.itberries2018.demo.websockets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itberries2018.demo.auth.entities.User;
import com.itberries2018.demo.mechanics.events.game.GameResult;
import com.itberries2018.demo.mechanics.events.game.Start;
import com.itberries2018.demo.mechanics.events.game.Turn;
import com.itberries2018.demo.mechanics.events.logic.Move;
import com.itberries2018.demo.mechanics.events.logic.Score;
import com.itberries2018.demo.mechanics.events.service.Connect;
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
    private final List<GameSession> games = Collections.synchronizedList(new ArrayList());
    private final Map<Long, GameSession> gameMap = new ConcurrentHashMap<>();
    public static final long TURN_DURATION_MILLS = 30 * 1000;


    public List<GameSession> getGames() {
        return games;
    }

    public RemotePointService(@NotNull UserService userService,
                              @NotNull ScoreRecordService scoreRecordService,
                              @NotNull ObjectMapper objectMapper) {
        this.userService = userService;
        this.scoreRecordService = scoreRecordService;
        this.objectMapper = objectMapper;
        this.service.scheduleAtFixedRate(new GameDispatcher(), 0, 1, TimeUnit.SECONDS);
    }

    private ScheduledExecutorService service = Executors.newScheduledThreadPool(1);


    private class GameDispatcher implements Runnable {

        @Override
        public void run() {
            synchronized (games) {
                for (GameSession game : games) {
                    synchronized (game) {
                            if (game.getStatus() == GameSession.Status.IN_GAME && (game.getLatestTurnStart()
                                    + TURN_DURATION_MILLS) < System.currentTimeMillis()) {
                                try {
                                    if (game.getTurn().toString().toLowerCase().equals("human")) {
                                        sendMessageToUser(game.getUfo().getId(), new Turn("ufo"));
                                        sendMessageToUser(game.getHuman().getId(), new Turn("ufo"));
                                    } else {
                                        sendMessageToUser(game.getHuman().getId(), new Turn("human"));
                                        sendMessageToUser(game.getUfo().getId(), new Turn("human"));
                                    }
                                    game.timeOut();
                                } catch (Exception ex) {
                                    logger.warn("ERROR SENDING MESSAGE FROM GAMEDISPATCHER");
                                }
                            }

                    }
                }
            }
        }
    }

    public void handleGameMessage(Message message, Long userId) throws IOException {
        final GameSession game = gameMap.get(userId);
        if (userId.equals(game.whoseTurn().getId())) {
            game.step((Move) message);
            if (game.getHuman().getId().equals(userId)) {
                Score score = new Score(game.getHuman().getScore(), game.getHuman().getName());
                ArrayList<Message> scoreMessage = new ArrayList<Message>();
                scoreMessage.add(score);
                try {
                    sendGameMessages(scoreMessage, game);
                } catch (Exception e) {
                    logger.error("ERROR BAD CROSS-MESENGER");
                }
                sendMessageToUser(game.getUfo().getId(), message);
                sendMessageToUser(game.getUfo().getId(), new Turn("ufo"));
            } else {
                Score score = new Score(game.getUfo().getScore(), game.getUfo().getName());
                ArrayList<Message> scoreMessage = new ArrayList<Message>();
                scoreMessage.add(score);
                try {
                    sendGameMessages(scoreMessage, game);
                } catch (Exception e) {
                    logger.error("ERROR BAD CROSS-MESENGER");
                }
                sendMessageToUser(game.getHuman().getId(), message);
                sendMessageToUser(game.getHuman().getId(), new Turn("human"));
            }
            if (game.getStatus() == GameSession.Status.HUMANS_WIN) {
                GameResult result = new GameResult(game.getHuman(), game.getUfo(), "HUMANS_WIN", game.getGlobalTimer());
                try {
                    finishGame(game, result);
                } catch (Exception e) {
                    logger.error("ERROR FINISH GAME");
                }
            } else if (game.getStatus() == GameSession.Status.UFO_WIN) {
                GameResult result = new GameResult(game.getUfo(), game.getHuman(), "UFO_WIN", game.getGlobalTimer());
                try {
                    finishGame(game, result);
                } catch (Exception e) {
                    logger.error("ERROR FINISH GAME");
                }
            }
        }
        // sendGameMessages(game.interact(message, userID), game); возможно, для взаимодейвтсвия нло и ракеты
    }

    public void sendGameMessages(@Nullable List<Message> messages, GameSession game) throws Exception {
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

    public void finishGame(GameSession game, GameResult gameResult) throws Exception {
        final Long winnerId = gameResult.getPayload().getWinner().getId();
        final int winnerScore = gameResult.getPayload().getWinner().getScore();
        final Long loserId = gameResult.getPayload().getLoser().getId();
        final int loserScore = gameResult.getPayload().getLoser().getScore();
        scoreRecordService.incrementScore(winnerId, winnerScore);
        scoreRecordService.incrementScore(loserId, -winnerScore);
        sendMessageToUser(winnerId, gameResult);
        sendMessageToUser(loserId, gameResult);
        sessions.get(winnerId).close();
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
            Connect message = new Connect("Connecting to your opponent");
            sendMessageToUser(humansUserId, message);
            sendMessageToUser(aliensUserId, message);

            final User humansUser = userService.findById(humansUserId);
            final User aliensUser = userService.findById(aliensUserId);

            final GameSession gameSession = new GameSession(new GamePlayer(aliensUser), new GamePlayer(humansUser));
            gameSession.start();
            sendMessageToUser(humansUserId, new Start(gameSession));
            sendMessageToUser(aliensUserId, new Start(gameSession));
            games.add(gameSession);
            gameMap.put(humansUserId, gameSession);
            gameMap.put(aliensUserId, gameSession);
            sendMessageToUser(humansUserId, new Turn("human"));
            sendMessageToUser(aliensUserId, new Turn("human"));
        } else {
            sendMessageToUser(userId, new Connect("Waiting for an opponent"));
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
            if (humans.contains(userId)) {
                humans.remove(userId);
            } else {
                aliens.remove(userId);
            }
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
                                winner, loser, "Your opponent disconnected", userGame.getGlobalTimer()
                        ));
                    } catch (IOException ignore) {
                        ignore.printStackTrace();
                        logger.error("ERROR CLOSING WEBSOCKET");
                    }
                    /*scoreRecordService.incrementScore(winner.getId(), winner.getScore());*/
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
