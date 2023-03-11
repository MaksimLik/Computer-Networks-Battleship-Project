#ifndef __SERVER_H__	
#define __SERVER_H__

#include <iostream>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <vector>
#include <player.h>
#include <mutex>
#include <thread>
#include <functional>

class Server
{
	private:
		int sockfd;
		unsigned int portno;
		unsigned int players;
		std::mutex searching_plaeyrs_mutex;
	
	public:
		Server();
		~Server();
		int getsockfd();
		void error(char * msg);
		void launch();
		int connection();
		int send(int socket, char* buffer, int size);
		void goodbye();
		void searchPlayers();
		int handleReceive(int);
		void newGame(Player*, Player*);
		void newPlayerCommand(int, std::string);
		void disconnection(int);
		Player* getPlayerByFd(int);
		void removePlayerFromQueue(int);
		bool checkIfPlayerExists(int);

		std::vector<Player*> waitingPlayers; 
		std::vector<Player*> playersInGame;



};

#endif
