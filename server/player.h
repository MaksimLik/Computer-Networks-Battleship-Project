#ifndef __PLAYER_H__	
#define __PLAYER_H__
#include <string>
#include <iostream>

class Player
{
private:
    int fd;
    int fd_opponent;
    std::string name;
    bool turn;
public:
    Player(int,std::string);
    std::string getName();
    int getFd();
    char board[10][10];
    bool readyToPlay;
    void displayPlayerBoard();
    void prepareBoard(char*);
    void setOpponent(int);

    int getFdOpponent();
    bool getTurn();
    void setTurn(bool);

    bool checkShot(std::string);
    bool checkLose();

};


#endif