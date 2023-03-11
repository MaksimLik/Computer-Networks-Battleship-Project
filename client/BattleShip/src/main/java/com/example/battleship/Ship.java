package com.example.battleship;

import java.util.ArrayList;

public class Ship {
    int size;
    ArrayList<Field> location;

    int numUndamagedParts;
    int numDamagedParts;

    Ship(int size, ArrayList<Field> location) {
        this.size = size;
        this.location = location;
        numUndamagedParts = size;
        numDamagedParts = 0;
    }

    boolean isDestroyed() {
        return numDamagedParts == size;
    }
}
