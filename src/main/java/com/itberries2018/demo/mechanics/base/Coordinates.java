package com.itberries2018.demo.mechanics.base;

public class Coordinates {
    private int xval;

    private int yval;

    public Coordinates() {
        this.xval = 0;
        this.yval = 0;
    }

    public Coordinates(int xval, int yval) {
        this.xval = xval;
        this.yval = yval;
    }

    public int getXval() {
        return xval;
    }

    public void setXval(int xval) {
        this.xval = xval;
    }

    public int getYval() {
        return yval;
    }

    public void setYval(int yval) {
        this.yval = yval;
    }

    @Override
    public String toString() {
        return "Coordinates{" + "x=" + xval + ", y=" + yval + '}';
    }
}
