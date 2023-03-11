#include <iostream>
#include <thread>
#include <cstdio>
#include <string.h>
#include <vector>
#include <string>
#include "./server.h"
#include "./player.h"

Server server;


int main(int argc, char** argv) {

    server.launch();
    server.searchPlayers();

    return 0;
}