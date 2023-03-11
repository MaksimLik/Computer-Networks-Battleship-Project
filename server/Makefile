LIBS = -std=c++11 -pthread -Wall
HEADERS=server.h player.h
FILES=main.cpp server.cpp player.cpp

main_file: $(FILES) $(HEADERS)
	g++ -o main $(FILES) $(LIBS) -I.