1. tar file extrahieren
2. OtwayRees/AuthenticationServer/AuthenticationServer.java

   Variable keyFile anpassen --> absoluter Path (Zeile 41)

3. javac OtwayRees/AuthenticationServer/AuthenticationServer.java 

AUTHENTICATIONSERVER
--------------------------------------------
1. java OtwayRees/AuthenticationServer/AuthenticationServer


CLIENT
--------------------------------------------
Im System sind 3 User konfiguriert:
   carina
   daniel
   michi

Es gibt 2 Möglichkeiten den Client zu starten 
   1. als Server in dieser Konfiguration wartet der Server auf eine eingehende Verbindung
   2. als Client in dieser Konfiguration versucht der Client sich am Server zu verbinden
    
1. javac OtwayRees/Client/Client.java
     
     Enter your name:michi 
      Start Communication Server for User(michi) : 1
         Start Communication Client for User(michi) : 2
         Usage: [ 1 | 2 ]:1

         In diesem Beispiel startet der Server und lauscht auf dem Port welches michi zugewiesen ist.

2. javac OtwayRees/Client/Client.java 

         Enter your name:daniel
            Start Communication Server for User(daniel) : 1
               Start Communication Client for User(daniel) : 2
               Usage: [ 1 | 2 ]:2
                  Enter the name of your Chat partner:michi

In diesem Beispiel verbindet sich der Client auf das Port welches michi zugewiesen ist.

START APP
------------------------------
1.AuthenticationServer starten
2. Client Communication Server starten  (user1)
3. Client Communication Client starten  (user2) Chat partner ist user1



