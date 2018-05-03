package com.itberries2018.demo.game;

import com.itberries2018.demo.models.ProfileData;
import org.jetbrains.annotations.NotNull;

import java.util.PriorityQueue;

public class GameSession {

    public enum Status {
        CREATED,
        READY_FOR_START,
        OVERED,
        IN_GAME
    }

    private enum Turn {
        UFO,
        HUMAN
    }

    @NotNull
    private final ProfileData ufo;

    @NotNull
    private final ProfileData human;

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

    private Turn turn;

    public GameSession(@NotNull ProfileData ufo, @NotNull ProfileData human) {
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
        paths[cells[ufoCoords.getY()][ufoCoords.getX()].getNumber()] =  -1;
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
    }

    public void end() {
        this.status = Status.OVERED;
    }

    public void step() {
        if (turn == Turn.HUMAN) {
            checkHumnasWin();
        } else {
            checkUfoWinCoords(this.map.getUfoCoords().getX(), this.map.getUfoCoords().getY());
        }
    }

}
