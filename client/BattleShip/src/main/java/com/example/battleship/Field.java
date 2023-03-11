package com.example.battleship;

public class Field {
    private int x;
    private int y;

    private int state; // state 0 - empty  1 - partOfShip 2 - damagedPartOfship

    public Field(int x, int y, int state) {
        this.x = x;
        this.y = y;
        this.state = state;
    }

    public Field(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

}
