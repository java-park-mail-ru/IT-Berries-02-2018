package com.itberries2018.demo.mechanics.game;

import com.itberries2018.demo.mechanics.events.logic.Move;
import com.itberries2018.demo.mechanics.player.GamePlayer;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.ScheduledExecutorService;

public class GameSession {

    public enum Turn {
        UFO,
        HUMAN
    }

    public enum Status {
        CREATED,
        READY_FOR_START,
        OVERED,
        IN_GAME,
        HUMANS_WIN,
        UFO_WIN
    }

    public List<GamePlayer> getPlayerList() {
        return playerList;
    }

    private final List<GamePlayer> playerList;

    @NotNull
    private final GamePlayer ufo;

    public GamePlayer getHuman() {
        return human;
    }

    @NotNull
    private final GamePlayer human;

    public GamePlayer getUfo() {
        return ufo;
    }

    @NotNull
    private final GameMap map;

    public GameMap getMap() {
        return map;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    private Status status;

    public Turn getTurn() {
        return turn;
    }

    public void setTurn(Turn turn) {
        this.turn = turn;
    }

    private Turn turn;

    private ScheduledExecutorService turns;

    public long getGlobalTimer() {
        return globalTimer;
    }

    public void setGlobalTimer(long globalTimer) {
        this.globalTimer = globalTimer;
    }

    private long globalTimer;

    public long getTurnTimer() {
        return turnTimer;
    }

    private long turnTimer;

    public GameSession(@NotNull GamePlayer ufo, @NotNull GamePlayer human) {
        playerList = new ArrayList<GamePlayer>();
        playerList.add(ufo);
        playerList.add(human);
        this.status = Status.CREATED;
        this.ufo = ufo;
        this.human = human;
        map = new GameMap(9, 9);
        while (!this.checkHumnasWin()) {
            map.reset();
        }
        turn = Turn.HUMAN;
        this.status = Status.READY_FOR_START;
    }

    public boolean checkHumnasWin() {
        PriorityQueue<Integer> queueOfCellForSetps = new PriorityQueue<Integer>();
        int[] hookup = new int[this.map.getCellCount()];
        int[] paths = new int[this.map.getCellCount()];
        UfoCoords ufoCoords = this.map.getUfoCoords().clone();
        MapCell[][] cells = this.map.getCells();
        CellCheckContainer[] cellsContainers = new CellCheckContainer[this.map.getCellCount()];
        int counter = 0;
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length; j++) {
                cellsContainers[counter] = new CellCheckContainer(cells[i][j]);
                if (i == ufoCoords.getY() && j == ufoCoords.getX()) {
                    cellsContainers[counter].setUsed(true);
                }
                counter++;
            }
        }
        paths[cells[ufoCoords.getY()][ufoCoords.getX()].getNumber()] = -1;
        MapCell startCell = cells[ufoCoords.getY()][ufoCoords.getX()];
        CellCheckContainer startCellContainer = cellsContainers[startCell.getNumber()];
        queueOfCellForSetps.add(startCellContainer.getCell().getNumber());
        int[][] adjacencyMatrix = map.getAdjacencyMatrix();
        while (!queueOfCellForSetps.isEmpty()) {
            Integer container = queueOfCellForSetps.poll();
            int[] ways = this.map.getAdjacencyMatrix()[container];
            for (int i = 0; i < ways.length; i++) {
                if (ways[i] == 1) {
                    CellCheckContainer to = cellsContainers[i];
                    if (!to.getUsed() && to.getCell().getOwner() != MapCell.Owner.ROCKET) {
                        to.setUsed(true);
                        queueOfCellForSetps.add(to.getCell().getNumber());
                        hookup[to.getCell().getNumber()] = hookup[container] + 1;
                        paths[to.getCell().getNumber()] = container;
                        if (this.checkUfoWinCoords(to.getCell().getX(), to.getCell().getY())) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private boolean checkUfoWinCoords(int ufoX, int ufoY) {
        return ufoX == 0
                || ufoX == this.map.getCells()[ufoY].length - 1
                || ufoY == 0
                || ufoY == this.map.getCells().length - 1;
    }

    public void start() {
        this.status = Status.IN_GAME;
        this.globalTimer = System.currentTimeMillis();
    }

    public void end() {
        this.globalTimer = System.currentTimeMillis() - this.globalTimer;
    }

    public GamePlayer whoseTurn() {
        if (!status.equals(Status.IN_GAME)) {
            return null;
        }
        if (turn == Turn.HUMAN) {
            return human;
        } else {
            return ufo;
        }
    }

    public void choseTurn() {
        if (turn == Turn.HUMAN) {
            turn = Turn.UFO;
        } else {
            turn = Turn.HUMAN;
        }
    }

    public void step(Move move) throws IOException {
        if (turn == Turn.HUMAN) {
            if (!this.map.setRocket(move.getTo())) {
                throw new IOException("No valid turn");
            }
            this.human.setTurns(this.human.getTurns() + 1);
            this.human.setScore(this.human.getScore() + 10);
            if (!checkHumnasWin()) {
                this.status = Status.HUMANS_WIN;
                this.end();
            } else {
                turn = Turn.UFO;
            }
            this.turnTimer = System.currentTimeMillis();
        } else {
            if (!this.map.setUfo(move.getTo())) {
                throw new IOException("No valid turn");
            }

            this.ufo.setTurns(this.ufo.getTurns() + 1);
            this.ufo.setScore(this.ufo.getScore() + 10);
            if (checkUfoWinCoords(this.map.getUfoCoords().getX(), this.map.getUfoCoords().getY())) {
                this.status = Status.UFO_WIN;
                this.end();
            } else {
                turn = Turn.HUMAN;
            }
            this.turnTimer = System.currentTimeMillis();
        }
    }

}
