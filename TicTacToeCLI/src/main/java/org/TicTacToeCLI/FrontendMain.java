package org.TicTacToeCLI;

import java.util.Scanner;

public class FrontendMain {
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

        replaygameloop:
        while (true) {

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
                    System.out.println("Ваш sessionId: " + sessionId);
                }

                case "connect" -> {
                    symbol = 'o';

                    String createdUser = userClient.createUser(name, symbol);
                    if (createdUser.isEmpty()) {
                        throw new Exception("Не удалось создать пользователя!");
                    }

                    System.out.println(createdUser);
                    userId = JsonParser.getUserId(createdUser);

                    System.out.print("Введите session id: ");
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

                default -> throw new Exception("ИДИ УЧИ УРОКИ ЧОРТ\nargument required: create|connect");
            }

            System.out.println("role:" + role + "\nsessionId:" + sessionId + "\nuserId:" + userId);

            while (true) {
                String sessionInfo = consoleClient.getSessionInfo(sessionId);
                System.out.println(
                        GameField.toString_(
                                JsonParser.getFieldArray(sessionInfo)
                        )
                );

                System.out.flush();
                if (!JsonParser.parseJSON(sessionInfo).get("result").equals(JsonParser.ResultValues.NOT_FINISHED.name())) {
                    var result = JsonParser.parseJSON(sessionInfo).get("result");
                    System.out.println(result);
                    break;
                }

                if ((
                        role.equals("create") && (boolean) JsonParser.parseJSON(sessionInfo).get("isHostTurn")
                ) || (
                        role.equals("connect") && !(boolean) JsonParser.parseJSON(sessionInfo).get("isHostTurn")
                )) {
                    int width = in.nextInt();
                    int height = in.nextInt();
                    var ok = consoleClient.placeSymbol(sessionId, userId, width, height);
                }

                Thread.sleep(500);
            }

            System.out.print("Хотите сыграть снова? (1|0)");
            int replay = in.nextInt();
            if (replay != 1) {
                break replaygameloop;
            }
        }

        System.out.print("Хорошего дня!");
    }
}
