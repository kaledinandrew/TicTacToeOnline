package org.TicTacToeCLI;

import org.TicTacToeCLI.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URLEncoder;
import java.util.*;

import static org.TicTacToeCLI.Connection.connect;
import static org.TicTacToeCLI.Variables.*;

import java.net.URL;


public class FrontendMain {
    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void main(String[] args) throws Exception {

        UserClient userClient = new UserClient();
        Console consoleClient = new Console();
        int hostId;
        int guestId;
        int sessionId;

//        Эти пользователи и сессия уже существуют
//        hostId = 13;
//        guestId = 16;
//        System.out.println(userClient.getUserInfo(hostId));
//        System.out.println(userClient.getUserInfo(guestId));
//        sessionId = 9;

//       Можно создать новые
        Scanner in = new Scanner(System.in);
        Scanner in_num = new Scanner(System.in);
        String name = "";
        int symbol = 0;
        String sessionInfo = "";

        System.out.println("Ввод имени и символа первого игрока (хоста):");
        name = in.nextLine();
        symbol = in_num.nextInt();
        hostId = JsonParser.getUserId(userClient.createUser(name, symbol));

        System.out.println("Ввод имени и символа второго игрока (гостя):");
        name = in.nextLine();
        symbol = in_num.nextInt();
        guestId = JsonParser.getUserId(userClient.createUser(name, symbol));

        System.out.println(userClient.getUserInfo(hostId));
        System.out.println(userClient.getUserInfo(guestId));

        // Хост создает сессию
        sessionInfo = consoleClient.startSession(hostId);
        System.out.println(sessionInfo);
        sessionId = JsonParser.getSessionId(sessionInfo);


        System.out.printf("SessionId = %d\n", sessionId);

        // Гость подключается к сессии
        String sessionConnectInfo = consoleClient.connectToSession(sessionId, guestId);
        // System.out.println(sessionConnectInfo);

        System.out.println("Start the game!\n");
        for (int i = 0; i < 9; i++) {
            String sessionInfoGame = consoleClient.getSessionInfo(sessionId);
            Boolean turn = JsonParser.getTurn(sessionInfoGame);

            String gameInfo = "";
            if (turn) {
                System.out.println("Хост делает ход");
                System.out.println("Ввод координат x, y:");
                int x = in_num.nextInt();
                int y = in_num.nextInt();

                gameInfo = consoleClient.placeSymbol(sessionId, hostId, x, y);
            } else {
                System.out.println("Гость делает ход");
                System.out.println("Ввод координат x, y:");
                int x = in_num.nextInt();
                int y = in_num.nextInt();

                gameInfo = consoleClient.placeSymbol(sessionId, guestId, x, y);
            }

            consoleClient.showField(sessionId);
            System.out.println("\nПродолжить игру? Да/Нет");
            String answ = in.nextLine();
            if (Objects.equals(answ, new String("Нет"))) {
                break;
            }

            clearScreen();
        }

        in.close();
        in_num.close();
        System.out.println("Игра окончена!");
    }
}
