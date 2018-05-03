package com.itberries2018.demo.game;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class GameMap {

    private int baseRocketValue = 20;

    @NotNull
    private final int width;
    @NotNull
    private final int height;
    @NotNull
    private final int[][] adjacencyMatrix;
    @NotNull
    private final MapCell[][] cells;
    @NotNull
    private UfoCoords ufoCoords;
    @NotNull
    private int cellCount;

    public int[][] getAdjacencyMatrix() {
        return adjacencyMatrix;
    }

    public MapCell[][] getCells() {
        return cells;
    }

    public int getCellCount() {
        return cellCount;
    }

    public void setCellCount(int cellCount) {
        this.cellCount = cellCount;
    }

    public UfoCoords getUfoCoords() {
        return ufoCoords;
    }

    public GameMap(@NotNull int height, @NotNull int width) {
        this.width = width;
        this.height = height;
        int cellCounter = 0;
        this.cells = new MapCell[height][];
        for (int i = 0; i < this.height; i++) {
            this.cells[i] = new MapCell[width + i % 2];
            for (int j = 0; j < this.cells[i].length; j++) {
                this.cells[i][j] = new MapCell(j, i, cellCounter);
                cellCounter++;
            }
        }
        this.cellCount = height * width + height / 2;
        this.adjacencyMatrix = new int[cellCount][cellCount];
        cellCounter = 0;
        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.cells[i].length; j++) {
                if (j + 1 < this.cells[i].length) {
                    this.adjacencyMatrix[cellCounter][cellCounter + 1] = 1;
                }
                if (j - 1 >= 0) {
                    this.adjacencyMatrix[cellCounter][cellCounter - 1] = 1;
                }
                if (i % 2 == 0) {
                    if ((i + 1 < this.height)) {
                        this.adjacencyMatrix[cellCounter][cellCounter + this.width + 1] = 1;
                        this.adjacencyMatrix[cellCounter][cellCounter + this.width] = 1;
                    }
                    if ((i - 1 >= 0)) {
                        this.adjacencyMatrix[cellCounter][cellCounter - this.width - 1] = 1;
                        this.adjacencyMatrix[cellCounter][cellCounter - this.width] = 1;
                    }
                } else {
                    if ((i + 1 < this.height)) {
                        if (j + 1 < this.cells[i].length) {
                            this.adjacencyMatrix[cellCounter][cellCounter + this.width + 1] = 1;
                        }
                        if (j - 1 >= 0) {
                            this.adjacencyMatrix[cellCounter][cellCounter + this.width] = 1;
                        }
                    }
                    if ((i - 1 >= 0)) {
                        if (j - 1 >= 0) {
                            this.adjacencyMatrix[cellCounter][cellCounter - this.width - 1] = 1;
                        }
                        if (j + 1 < this.cells[i].length) {
                            this.adjacencyMatrix[cellCounter][cellCounter - this.width] = 1;
                        }
                    }
                }
                cellCounter++;
            }
        }
        this.setUfo();
        this.generateRockets(baseRocketValue);
    }

    private void setUfo() {
        try {
            int ufoStartY = this.height / 2;
            int ufoStartX = this.cells[ufoStartY].length / 2;
            this.cells[ufoStartY][ufoStartX].setOwner(MapCell.Owner.UFO);
            this.ufoCoords = new UfoCoords(ufoStartY, ufoStartX);
        } catch (Exception e) {
            throw e;
        }
    }

    private void generateRockets(int rocketCount) {
        for (int i = 0; i < rocketCount; i++) {
            int rocketY = (int) Math.round((Math.random() * (this.height - 1)));
            int rocketX = (int) Math.round((Math.random() * (this.cells[rocketY].length - 1)));
            if (this.cells[rocketY][rocketX].getOwner() == MapCell.Owner.UFO
                    || this.cells[rocketY][rocketX].getOwner() == MapCell.Owner.ROCKET) {
                i--;
            } else {
                this.cells[rocketY][rocketX].setOwner(MapCell.Owner.ROCKET);
            }
        }
    }

    public ArrayList<MapCell> getRockets() {
        ArrayList<MapCell> rockets = new ArrayList<MapCell>();
        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.cells[i].length; j++) {
                MapCell cell = this.cells[i][j];
                if (cell.getOwner() == MapCell.Owner.ROCKET) {
                    rockets.add(cell);
                }
            }
        }
        return rockets;
    }

    public void reset() {
        for (int i = 0; i < this.cells.length; i++) {
            for (int j = 0; j < this.cells[i].length; j++) {
                if (i != ufoCoords.getY() && j != ufoCoords.getX()) {
                    this.cells[i][j].setOwner(MapCell.Owner.NOTHING);
                }
            }
        }
        this.generateRockets(baseRocketValue);
    }

}