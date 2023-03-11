#include "./player.h"



Player::Player(int fd, std::string name) {
    this->fd = fd;
    this->name = name;
    this->readyToPlay = false;
    this->fd_opponent = -1;
}

std::string Player::getName() {
    return this->name;
}


int Player::getFd() {
    return this->fd;
}

void Player::prepareBoard(char* buffer) {
    for (int i = 0; i < 10; i++) {
        for (int j = 0; j < 10; j++) {
            this->board[i][j] = buffer[10*i + j];
        }
    }
}

void Player::displayPlayerBoard() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
            std::cout << this->board[i][j];
        }
        std::cout << std::endl;
    }
}

void Player::setOpponent(int fd_op) {
    this->fd_opponent = fd_op;
}

int Player::getFdOpponent() {
    return this->fd_opponent;
}


void Player::setTurn(bool turn) {
    this->turn = turn;
  
}

bool Player::getTurn() {
    return this->turn;
}

bool Player::checkShot(std::string buf) {
 

    int x = stoi(buf.substr(1))-1;
    int y = (int)buf[0] - (int)'A';

    std::cout << x << " --- " << y << std::endl;

    if (board[x][y] == '1') {
        board[x][y] = '2';
        return true;
    }

    return board[x][y] == '1'; 

}


bool Player::checkLose() {
     for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
            if(this->board[i][j] == '1') return false;
        }
       
    }

    return true;
}