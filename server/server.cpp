#include "./server.h"

	Server::Server()
	{
		this->sockfd = 0;
		this->portno = 0;
		this->players = 0;
	
	}

	Server::~Server()
	{
		close(this->sockfd);
	}

	int Server::getsockfd()
	{
		return this->sockfd;
	}

	void Server::error(char * msg)
	{
		perror(msg);
		exit(1);
	}

	void Server::launch()
	{
		int port = -1;
		struct sockaddr_in serv_addr;

		while(port < 0 || port > 65535)
		{
			std::cout << std::endl << "Wpisz numer portu (typ <<unsigned int>>)" << std::endl;
			std::cin >> port;
		}

		this->sockfd = socket(AF_INET, SOCK_STREAM, 0);
		if (this->sockfd < 0)
			error((char *) "ERROR opening socket");


		bzero((char *) &serv_addr, sizeof(serv_addr));
		this->portno = port;
		serv_addr.sin_family = AF_INET;
		serv_addr.sin_port = htons(this->portno);
		serv_addr.sin_addr.s_addr = INADDR_ANY;
		std::cout << "Listening" << std::endl;
		if (bind(this->sockfd, (struct sockaddr *) &serv_addr, sizeof(serv_addr)) < 0)
			error((char *) "ERROR binding");

		listen(this->sockfd, 2);
	}

	int Server::connection()
	{
		int newsockfd;
		struct sockaddr_in cli_addr;
		unsigned int clilen = sizeof(cli_addr);
		newsockfd = accept(this->sockfd, (struct sockaddr *) &cli_addr, &clilen);
		std::cout << "Client fd : " << newsockfd <<  " accepted" << std::endl;
		if (newsockfd < 0)
			error((char *) "ERROR on accept");
		this->players++;

		return newsockfd;
	}

	bool Server::checkIfPlayerExists(int fd) {
		for (auto* p : this->playersInGame) {
			if (p->getFd() == fd) return true;
		}
		return false;
	}


	void Server::disconnection(int socket) {
		std::cout << "rozlaczono socket nr: " << socket << std::endl; 
		close(socket);
		
		//this->getPlayerByFd

	}

	Player* Server::getPlayerByFd(int fd) {
		for (auto* p : this->playersInGame) {
			if (p->getFd() == fd) return p;
		}
		return NULL;
	}

	void Server::removePlayerFromQueue(int socket) {
		int i = 0;
		for (auto* p : this->playersInGame) {
			if (p->getFd() == socket) {
				std::cout << "1. Usunieto gracza o id " << socket << std::endl;
				this->playersInGame.erase(this->playersInGame.begin()+i);
				return;	
			}
			i++;
		}
		i = 0;
		for (auto* p : this->waitingPlayers) {
			if (p->getFd() == socket){
				std::cout << "2. Usunieto gracza o id " << socket << std::endl;
				this->waitingPlayers.erase(this->waitingPlayers.begin()+i);
				return;
			} 
			i++;
		}
	}


	void Server::searchPlayers() {
		while (true) {
			int fd = this->connection();
			this->handleReceive(fd);

		}
	}



	int Server::send(int socket, char* buffer, int size)
	{
		int n;
		std::cout << "Wysylanie wiadomosci" << std::endl;
		n = write(socket, buffer, size);
		std::cout << "Wyslano wiadomosc o rozmiarze: " << size << std::endl;
		
		return n;
	}


	int Server::handleReceive(int socket) {

		// odczytanie wiadomosci

		int msgSize;
		int n;
		char buf[4096] {}; // bufor do calej wiadomosci
		char messSize[3] {}; // rozmiar bufora
		char command[10]; // komenda
		char info[100]; // informacja po komendzie

	
		int recv_size = recv(socket, messSize, 3, MSG_WAITALL);
		std::cout << recv_size << std::endl;
		if (recv_size != 3){
			if (this->checkIfPlayerExists(socket)) {
				Player* player = this->getPlayerByFd(socket);
				Player* opp  = this->getPlayerByFd(player->getFdOpponent());
				this->removePlayerFromQueue(socket);
				if (opp != NULL) {
					this->send(opp->getFd(), (char*)"disconnect_A1", strlen((char*)"disconnect_A1"));
					this->removePlayerFromQueue(opp->getFd());

				}
				delete player;
				delete opp;
				return -1;
			}
			return -1;
			
		}
		sscanf(messSize, "%d", &msgSize);
	


		std::cout << "Rozmiar wiadomosci :" <<  msgSize << std::endl;
		if ((n = recv(socket, buf, msgSize, MSG_WAITALL)) != msgSize){
				if (this->checkIfPlayerExists(socket)) {
				// wyslanie do gracza inforamcji ze rozloczono jego przeciwnika
			//	this->removePlayerFromQueue(socket);

			if (this->checkIfPlayerExists(socket)) {
					Player* player = this->getPlayerByFd(socket);
					Player* opp  = this->getPlayerByFd(player->getFdOpponent());
					this->removePlayerFromQueue(socket);
					if (opp != NULL) {
						this->send(opp->getFd(), (char*)"disconnect_A1", strlen((char*)"disconnect_A1"));
						this->removePlayerFromQueue(opp->getFd());

					}
					delete player;
					delete opp;
					return -1;
			}

				return -1;
			}
			return -1;
			// usuniecie rozlaczenie gracza i jego przeciwnika			
	 	}
		std::cout << buf << std::endl;
		sscanf(buf, "%s %s", command, info);



		// obsluga roznych komend

		if (!strcmp(command, "newplayer")) {
			// dodawanie nowego gracza
			this->newPlayerCommand(socket, std::string(info));
 		} 
		else if (!strcmp(command, "newboard")) {

			Player* player = this->getPlayerByFd(socket);	
			player->readyToPlay = true;
			player->prepareBoard(info);

		}

		else if (!strcmp(command, "shot")) {
			Player* player = this->getPlayerByFd(socket);
			Player* opponent = this->getPlayerByFd(player->getFdOpponent());
			
			// sprawdzamy czy trafilismy
			std::cout << info << std::endl;
			if (opponent->checkShot(std::string(info))) {


				if (opponent->checkLose()) {
					char mess[20] = "win_";
					strcat(mess, info);
					std::cout << std::string(mess) << std::endl;
					this->send(player->getFd(), mess, strlen(mess));


					char mess2[20] = "lose_";
					strcat(mess, info);
					std::cout << std::string(mess2) << std::endl;
					this->send(opponent->getFd(), mess2, strlen(mess2));

				}
				else {
					char mess[20] = "hit_";
					strcat(mess, info);
					std::cout << std::string(mess) << std::endl;
					this->send(player->getFd(), mess, strlen(mess));
				}



			}else {
				char mess[20] = "miss_";
				strcat(mess, info);
				this->send(player->getFd(), mess, strlen(mess));
			}

			char mess[20] = "yourmove_";
			strcat(mess, info);
			this->send(opponent->getFd(), mess, strlen(mess));

		}

		// clerowanie socketu 

		int k = 0;
		char clearbuffer[1024];
		while((k = recv(socket, clearbuffer, 1024, 0) > 0))

		return n;

	} 


	void Server::newPlayerCommand(int socket, std::string info) {
		Player* player = new Player(socket, info);
			this->searching_plaeyrs_mutex.lock();
				this->waitingPlayers.push_back(player);
				if (this->waitingPlayers.size() == 2) {     
					// odpalenie watku z nowa gra 

					Player* player1 = this->waitingPlayers[0];
					Player* player2 = this->waitingPlayers[1];
					player1->setOpponent(player2->getFd());
					player2->setOpponent(player1->getFd());

					this->playersInGame.push_back(player1);
					this->playersInGame.push_back(player2);

					std::thread newGameThread(std::bind(&Server::newGame, this, player1, player2));
					this->waitingPlayers.clear();
					newGameThread.detach();
				}
			this->searching_plaeyrs_mutex.unlock();
	}


	void Server::newGame(Player* p1, Player* p2) {
		std::cout << "Odpalam gre" << std::endl;
    	std::cout << "Graja " << p1->getName() << " oraz " << p2->getName() << std::endl;

		if(this->send(p1->getFd(), (char*)"run", 3) == -1) return;
    	if(this->send(p2->getFd(), (char*)"run", 3) == -1) return; 

    	std::cout << "Oczekiwanie na wybranie statkow" << std::endl;

		// przygotowanie statkow do gry - komenda newBoard
		if (this->handleReceive(p1->getFd()) == -1 ) return;
		if (this->handleReceive(p2->getFd()) == -1 ) return;

		// std::cout << "Plansza gracza 1" << std::endl;
		// p1->displayPlayerBoard();

		// std::cout << "Plansza gracza 2" << std::endl;
		// p2->displayPlayerBoard();

		// plansze przygotowane, odpalam rozgrywke

		srand (time(NULL));
  		int p1_turn = rand() % 2;
		int p2_turn = p1_turn == 1  ? 0 : 1;

		if (p1_turn) {
			if(this->send(p1->getFd(), (char*)"ready_yt", 8) == -1) return;
			p1->setTurn(true);
		} // yt - your turn
		else {
			if(this->send(p1->getFd(), (char*)"ready_op", 8) == -1)return; // tura 2 gracza
			p1->setTurn(false);
		}

		if (p2_turn) 
		{
			if(this->send(p2->getFd(), (char*)"ready_yt", 8) == -1) return; ; //  
			p2->setTurn(true);
		}
		else {
			if(this->send(p2->getFd(), (char*)"ready_op", 8) == -1)return; 
			p2->setTurn(false);
		} 

		while (true)
		{
			if (p1->getTurn())
			{
				int n = this->handleReceive(p1->getFd());
				if (n == -1) return;
				p1->setTurn(false);
				p2->setTurn(true);
				std::cout << "Tura gracza 1" << std::endl;
			}

			if (p2->getTurn()) {
				int n = this->handleReceive(p2->getFd());
				if (n == -1) return;
				p1->setTurn(true);
				p2->setTurn(false);
				std::cout << "Tura gracza 2" << std::endl;
			}
		}

	}


	void Server::goodbye()
	{
		this->players--;
	}
	