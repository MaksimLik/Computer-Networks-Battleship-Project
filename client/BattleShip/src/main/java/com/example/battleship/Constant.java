package com.example.battleship;

public class Constant {
    final static String RUN = "run";
    final static String READY = "ready";

    final static String MY_BOARD = "my_board";

    final static String ENEMY_BOARD = "enemy_board";

    final static char VERTICAL = 'v';
    final static char HORIZONTAL = 'h';
    final static int BOARD_SIZE = 10;

    // my board properties
    final static int WATER = 0;
    final static int SHIP_PART = 1;
    final static int DAMAGED_PART_SHIP = 2;

    final static int DESTROYED_SHIP = 3;

    // enemy board properties
    final static int MISSED_ATTACKED_FIELD = 4;

    final static int HIT_ATTACKED_FIELD = 5;

    final static int DESTROYED_SHIP_ENEMY_BOARD = 6;

}
