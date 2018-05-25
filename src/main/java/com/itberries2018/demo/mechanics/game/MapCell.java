package com.itberries2018.demo.mechanics.game;

import org.jetbrains.annotations.NotNull;

public class MapCell {

    public enum Owner {
        NOTHING,
        UFO,
        ROCKET
    }

    @NotNull
    private final int cellX;

    @NotNull
    private final int cellY;

    @NotNull
    private final int number;

    public int getNumber() {
        return number;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    private Owner owner = Owner.NOTHING;

    public int getX() {
        return cellX;
    }

    public int getY() {
        return cellY;
    }

    MapCell(@NotNull int cellX, @NotNull int cellY, @NotNull int number) {
        this.cellX = cellX;
        this.cellY = cellY;
        this.number = number;
    }

}
