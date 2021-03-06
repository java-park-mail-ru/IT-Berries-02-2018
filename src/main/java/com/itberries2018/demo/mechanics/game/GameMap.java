package com.itberries2018.demo.mechanics.game;

import com.itberries2018.demo.mechanics.base.Coordinates;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class GameMap {

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getBaseRocketValue() {

        return baseRocketValue;
    }

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
        int ufoStartY = this.height / 2;
        int ufoStartX = this.cells[ufoStartY].length / 2;
        this.setUfo(new Coordinates(ufoStartX, ufoStartY));
    }

    public boolean setUfo(Coordinates coordinates) {
        try {
            if (this.ufoCoords != null) {
                MapCell oldCell = this.cells[this.ufoCoords.getY()][this.ufoCoords.getX()];
                freeCell(oldCell);
            }
            int ufoY = coordinates.getYval();
            int ufoX = coordinates.getXval();
            MapCell cell = this.cells[ufoY][ufoX];
            if (!cell.getOwner().equals(MapCell.Owner.NOTHING)) {
                return false;
            }
            cell.setOwner(MapCell.Owner.UFO);
            blockCell(cell);
            this.ufoCoords = new UfoCoords(ufoY, ufoX);
        } catch (Exception e) {
            throw e;
        }
        return true;
    }

    private void generateRockets(int rocketCount) {
        for (int i = 0; i < rocketCount; i++) {
            int rocketY = (int) Math.round((Math.random() * (this.height - 1)));
            int rocketX = (int) Math.round((Math.random() * (this.cells[rocketY].length - 1)));
            MapCell cell = cells[rocketY][rocketX];
            if (!setRocket(cell)) {
                i--;
            }
        }
    }

    public boolean setRocket(Coordinates coordinates) {
        int ymoveValue = coordinates.getYval();
        int xmoveValue = coordinates.getXval();
        if (xmoveValue < 0 || xmoveValue > cells[0].length || ymoveValue < 0 || ymoveValue > cells.length) {
            return false;
        }
        return setRocket(cells[ymoveValue][xmoveValue]);
    }

    public boolean setRocket(MapCell cell) {
        if (!cell.getOwner().equals(MapCell.Owner.NOTHING)) {
            return false;
        } else {
            cell.setOwner(MapCell.Owner.ROCKET);
            blockCell(cell);
            return true;
        }
    }

    public void blockCell(MapCell cell) {
        int cellNumber = cell.getNumber();
        for (int i = 0; i < this.adjacencyMatrix[cellNumber].length; i++) {
            if (this.adjacencyMatrix[cellNumber][i] == 1) {
                this.adjacencyMatrix[i][cellNumber] = 0;
            }
        }
    }

    public void freeCell(MapCell cell) {
        int cellNumber = cell.getNumber();
        for (int i = 0; i < this.adjacencyMatrix[cellNumber].length; i++) {
            if (this.adjacencyMatrix[cellNumber][i] == 1) {
                this.adjacencyMatrix[i][cellNumber] = 1;
            }
        }
        cell.setOwner(MapCell.Owner.NOTHING);
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