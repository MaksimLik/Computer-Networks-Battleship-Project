package com.example.battleship;

import java.util.ArrayList;

public class Game {

    private boolean gameInit = false;
    private boolean shipsPut = false;
    private boolean gameEnd = false;
    private Board board;
    private Player player;

    public Game() {
        board = new Board();
    }

    public boolean putShipOnBoard(Field f, char dir, int size) {
        if (board.possiblePutShip(f, dir, size)) {
            ArrayList<Field> location = new ArrayList<>();

            for (int i = 0; i < size; i++) {
                if (dir == Constant.HORIZONTAL) {
                    location.add(new Field(f.getX() , f.getY()+i, Constant.SHIP_PART));
                    board.boardGame[f.getX()][f.getY()+i].setState(Constant.SHIP_PART);
                }
                else if (dir == Constant.VERTICAL) {
                    location.add(new Field(f.getX()+i, f.getY() , Constant.SHIP_PART));
                    board.boardGame[f.getX()+i][f.getY()].setState(Constant.SHIP_PART);
                }
            }
            Ship s = new Ship(size, location);
            player.addShip(s);
            System.out.println("Postawiono statek");
            return true;
        }
        return false;
    }





    public boolean isGameInit() {
        return gameInit;
    }

    public void setGameInit(boolean gameInit) {
        this.gameInit = gameInit;
    }

    public boolean isGameEnd() {
        return gameEnd;
    }

    public void setGameEnd(boolean gameEnd) {
        this.gameEnd = gameEnd;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public boolean isShipsPut() {
        return shipsPut;
    }

    public void setShipsPut(boolean shipsPut) {
        this.shipsPut = shipsPut;
    }
}



