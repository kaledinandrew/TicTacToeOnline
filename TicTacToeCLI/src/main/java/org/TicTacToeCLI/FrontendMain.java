package org.TicTacToeCLI;

import org.TicTacToeCLI.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;


public class FrontendMain {
    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            throw new Exception("argument required: create|connect");
        }
        System.out.print("name: ");
        Scanner in = new Scanner(System.in);
//        String name = in.nextLine();
        String name = "maxim";
        in.close();
        String role = args[0];
        int symbol;
        int sessionId;
        int userId;
        UserClient userClient = new UserClient();
        Console consoleClient = new Console();
        switch (role) {
            case "create" -> {
                symbol = 'x';

                String createdUser = userClient.createUser(name, symbol);
                if (createdUser.isEmpty()) {
                    throw new Exception("Не удалось создать пользователя!");
                }
                userId = JsonParser.getUserId(createdUser);

                String startedSession = consoleClient.startSession(userId);
                if (startedSession.isEmpty()) {
                    throw new Exception("Не удалось создать сессию!");
                }
                sessionId = JsonParser.getSessionId(startedSession);
                System.out.println("sessionId: " + sessionId);
            }
            case "connect" -> {
                symbol = 'o';

                String createsUser = userClient.createUser(name, symbol);
                if (createsUser.isEmpty()) {
                    throw new Exception("Не удалось создать пользователя!");
                }
                userId = JsonParser.getUserId(createsUser);

                System.out.print("session id: ");
                in = new Scanner(System.in);
                sessionId = in.nextInt();
                in.close();
                var createdSession = consoleClient.connectToSession(sessionId, userId);
                if (createdSession.isEmpty()) {
                    throw new Exception("Не удалось подключиться к сессии!");
                }
                System.out.println(createdSession);
            }
            default -> throw new Exception("ИДИ УЧИ УРОКИ\nargument required: create|connect");
        }
        System.out.println("role:"+role+"\nsessionId:"+sessionId+"\nuserId:"+userId);
        while(true) {
        }
    }
}
