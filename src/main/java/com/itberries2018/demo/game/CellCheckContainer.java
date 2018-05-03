package com.itberries2018.demo.game;

import org.jetbrains.annotations.NotNull;

public class CellCheckContainer {

    public MapCell getCell() {
        return cell;
    }

    @NotNull
    private MapCell cell;
    @NotNull
    private boolean used = false;

    public void setUsed(boolean used) {
        this.used = used;
    }

    public boolean getUsed() {
        return this.used;
    }

    public CellCheckContainer(@NotNull MapCell cell) {
        this.cell = cell;
    }
}
