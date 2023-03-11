package com.example.battleship;

public final class GameHolder {

    private Game game;
    private final static GameHolder INSTANCE = new GameHolder();

    private GameHolder() {}

    public static GameHolder getInstance() {
        return INSTANCE;
    }

    public void setGame(Game g) {
        this.game = g;
    }

    public Game getGame() {
        return this.game;
    }
}