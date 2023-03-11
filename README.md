# Sieci Komputerowe Bitwa w statki
Bitwa w statki online, serwer TCP, Klient

## Wprowadzenie

Projekt polega na tworzeniu aplikacji (gry) dla nieograniczonej ilości osób.
Projekt włącza w swój zestaw - serwer w języku C i klient w języku Java. Server jest
obsługiwany po przez 'Console', sama gra ma GUI (JavaFX).
####
Na dzisiejszy moment (w teorii) to działa w +/- taki sposób, że 
serwer jest wielowątkowym. Wątek podstawowy służy do tworzenia serwera i sprawdzianu aktualnego stanu: numer portu, liczba graczy, liczba gier. 
Dalej w wątku serwer cały czas szuka nowych klientów. 
Kiedy mamy połączenie z dwoma klientami tworzy się nowy wątek (pokój), w którym odbywa się gra. 
Klient przy połączeniu wysyła komunikat do serwera, że jest gotowy. I ropoczyna się gra.
Jak jeden z graczy wyłącza grę albo ma jakąś awarię to serwer informuje drugiego gracza i zamyka grę.
####
P.S. To nie jest cały opis, opis będzie rozszerzał się w miarę możliwości.
