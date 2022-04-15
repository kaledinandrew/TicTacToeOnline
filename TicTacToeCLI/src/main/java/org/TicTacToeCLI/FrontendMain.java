package org.TicTacToeCLI;

import org.TicTacToeCLI.*;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;


public class FrontendMain {
    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void main(String[] args) throws Exception {
        UserClient userClient = new UserClient();
        Console consoleClient = new Console();

        System.out.println("Добро пожаловать в TicTacToeOnline!");
        System.out.println("Введите имя пользователя и желаемый символ (числовой):");

        Scanner in = new Scanner(System.in);
        String name = in.nextLine();
        int symbol = in.nextInt();
        in.close();

        String userId = "";
        String userResponse = userClient.createUser(name, symbol);

        if (!userResponse.isEmpty()) {
            userId = JsonParser.parseJSON(userResponse).get("useId").toString();
            System.out.println("Пользователь успешно создан!");
        } else {
            System.out.println("Не удалось создать пользователя!");
            System.exit(1);
        }

        String botId = "";
        String botResponse = userClient.createUser("Steve Vai", 9);
        if (!botResponse.isEmpty()) {
            botId = JsonParser.parseJSON(botResponse).get("useId").toString();
            System.out.println("Бот успешно создан!");
        } else {
            System.out.println("Не удалось создать бота!");
            System.exit(1);
        }

        String response = consoleClient.startSession(userId);
        String sessionId = JsonParser.parseJSON(response).get("sessionId").toString();

        var connection = consoleClient.connectToSession(sessionId, botId);
        if (Objects.nonNull(connection)) {
            System.out.println("Игра началась!");
        } else {
            System.exit(1);
        }

        int filledCells = 0;
        int mn = consoleClient.showField(connection);

        int coin = 1;

        while (filledCells != mn) {
            clearScreen();
            consoleClient.showField(connection);

            if (coin == 1) {
                System.out.println("Ход игрока\nВвод координат:");
                in = new Scanner(System.in);
                Integer x = in.nextInt();
                Integer y = in.nextInt();
                in.close();

                // check if x, y are valid ...
                consoleClient.placeSymbol(sessionId, userId, x, y);
                coin = 0;
            } else {
                System.out.println("Ход бота");
                Integer x = (int) (Math.random() * 3);
                Integer y = (int) (Math.random() * 3);

                // check if x, y are valid ...
                consoleClient.placeSymbol(sessionId, botId, x, y);
                // wait for 2 seconds ...
                coin = 1;
            }

            filledCells++;
        }

        System.out.println("Игра окончена!");
        connection.disconnect();
    }
}
