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
        String name = in.nextLine();
        String role = args[0];
        int symbol;
        int sessionId = 0;
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

                String createdUser = userClient.createUser(name, symbol);
                if (createdUser.isEmpty()) {
                    throw new Exception("Не удалось создать пользователя!");
                }
                System.out.println(createdUser);
                userId = JsonParser.getUserId(createdUser);

                System.out.print("session id: ");
                in = new Scanner(System.in);
                if (in.hasNextLine()) {
                    sessionId = Integer.parseInt(in.nextLine());
                }
                var createdSession = consoleClient.connectToSession(sessionId, userId);
                if (createdSession.isEmpty()) {
                    throw new Exception("Не удалось подключиться к сессии!");
                }
                System.out.println(createdSession);
            }
            default -> throw new Exception("ИДИ УЧИ УРОКИ\nargument required: create|connect");
        }
        System.out.println("role:"+role+"\nsessionId:"+sessionId+"\nuserId:"+userId);
        String sessionInfo = consoleClient.getSessionInfo(sessionId);
        do  {
            System.out.println(JsonParser.parseJSON(sessionInfo).get("field"));
            if ((
                    role.equals("create") && (boolean)JsonParser.parseJSON(sessionInfo).get("isHostTurn")
            ) || (
                    role.equals("connect") && !(boolean)JsonParser.parseJSON(sessionInfo).get("isHostTurn")
            )) {
                int width = in.nextInt();
                int height = in.nextInt();
                consoleClient.placeSymbol(sessionId, userId, width,height);
            }
            Thread.sleep(500);
        } while (JsonParser.getSessionResult(sessionInfo).equals(JsonParser.ResultValues.NOT_FINISHED.name()));
    }
}
