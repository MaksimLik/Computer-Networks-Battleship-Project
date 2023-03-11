package com.example.battleship;

import java.util.ArrayList;

public class Player {
    private String name;
    private Client client;
    private ArrayList<Ship> ships;
    public int[] sizeOfShips;

    private boolean turn;

    // do testu
    Player() {
        this.ships = new ArrayList<>();
        this.sizeOfShips = new int[] {3, 3, 4, 4, 5};
    }
    Player(String name, Client client) {
        this.name = name;
        this.client = client;
        this.ships = new ArrayList<>();
        this.sizeOfShips = new int[] {3, 3, 4, 4, 5};
    }

    public String getName() {
        return name;
    }

    public Client getClient() {
        return client;
    }

    public ArrayList<Ship> getShips() {
        return ships;
    }

    public void addShip(Ship s) {
        ships.add(s);
    }

    public void setTurn(boolean turn) {
        this.turn = turn;
    }
    public boolean getTurn() {
        return this.turn;
    }
}
