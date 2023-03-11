# Computer-Networks-Battleship-Project
## Introduction
The project includes a server (C++) and a client (Java). The server is served in the console, while the client is in GUI(JavaFX)
The game is designed for an infinite number of players.
####
The server is multithreaded. The main thread is used to create the server and check its current status, including the host number, number of players, and number of games. Additionally, the server is always looking for new clients.

When two clients connect, a new stream (room) is created for the game. Once a client connects, it send a message to the server indicating that it's ready to begin the game.

If one of the players disconnects or experiences some kind of failure, the server informs the other player about the issue and closes the game.

## Screens of game and little description:

![BattleSheep](https://user-images.githubusercontent.com/72620745/224512936-6fdca864-ac61-4868-a075-8ccc58e0618a.png)

You should write your nickname and wait for your opponent to do the same.

![3](https://user-images.githubusercontent.com/72620745/224513007-973ddcda-6194-41de-ab0a-0eb1241e494e.png)

After you and your opponent place your ships, you can start shooting at the map

![5](https://user-images.githubusercontent.com/72620745/224513057-bb84eb30-6eee-45f8-8c36-3f06bfe1ce5b.png)

If one of you breaks the connection, the communicator will inform and the game will end.
If you hit the target, it will be highlighted in red.

## How to open this game on your computer

1. Open the server file in the server folder by running "./main" and specify a port (e.g. 1234).
2. Open the client file in IntelliJ and configure two games, then compile them.
3. The client will connect to the server at the address "localhost:(your port number, which you specified when running the server)." You can modify this address in the code if needed.
