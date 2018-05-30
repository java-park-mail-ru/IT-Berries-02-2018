package com.itberries2018.demo.mechanics.game;

import org.jetbrains.annotations.NotNull;

public class UfoCoords implements Cloneable {

    @NotNull
    private int ufoX;
    @NotNull
    private int ufoY;

    public int getX() {
        return ufoX;
    }

    public int getY() {
        return ufoY;
    }

    public UfoCoords(int ufoY, int ufoX) {
        this.ufoY = ufoY;
        this.ufoX = ufoX;
    }

    @Override
    public UfoCoords clone() {
        try {
            return (UfoCoords) super.clone();
        } catch (CloneNotSupportedException ex) {
            throw new InternalError();
        }
    }
}
