package org.TicTacToeCLI;

import java.util.Map;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class FrontendMain {

    static class Config {
        static ServiceUrls ReadConfig() throws FileNotFoundException {
            String data = "";
            File myObj = new File("config.json");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                data = myReader.nextLine();
            }
            myReader.close();
            Map<String, Object> map = JsonParser.parseJSON(data);
            String baseUrl = (String) map.get("baseUrl");
            return new ServiceUrls(baseUrl);
        }
    }
    public static void main(String[] args) throws Exception {

        Scanner in = new Scanner(System.in);
        String role;

        if (args.length < 1) {
            System.out.print("Создать сессию или присоединиться?\nВведите create или connect: ");
            role = in.nextLine();
            while (!role.equals("create") & !role.equals("connect")) {
                System.out.flush();
                System.out.print("Введен неверный аргумент\nПопробуйте еще раз: ");
                role = in.nextLine();
            }
        } else {
            role = args[0];
        }
        ServiceUrls urls = new ServiceUrls("");
        try {
            urls = Config.ReadConfig();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }

        System.out.print("Ваше имя: ");
        String name = in.nextLine();
        int symbol;
        int sessionId = 0;
        int userId;
        boolean isHost = false;
        UserClient userClient = new UserClient(urls);
        Console consoleClient = new Console(urls);
        replaygameloop:
        while (true) {

            switch (role) {
                case "create" -> {
                    symbol = 'x';
                    isHost = true;
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
                    isHost = false;
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
                    var result = (String) JsonParser.parseJSON(sessionInfo).get("result");
                    String message = "Ты проиграл... Рофлан поминки. Не затащил.";
                    if ((result.equals(JsonParser.ResultValues.HOST_WIN.name()) && isHost) || (result.equals(JsonParser.ResultValues.GUEST_WIN.name()) && !isHost)) {
                        message = "Ты выиграл!!! Команда TicTacToeOnline гордится тобой!";
                    } else if (result.equals(JsonParser.ResultValues.DRAW.name())) {
                        message = "Ничья. Играем до трех.";
                    }
                    System.out.println(message);
                    break;
                }

                if ((
                        role.equals("create") && (boolean) JsonParser.parseJSON(sessionInfo).get("isHostTurn")
                ) || (
                        role.equals("connect") && !(boolean) JsonParser.parseJSON(sessionInfo).get("isHostTurn")
                )) {
                    System.out.print("Введите координаты x, y: ");
                    int width = in.nextInt();
                    int height = in.nextInt();
                    while (width < 0 || height < 0 || width >= 3 || height >= 3 || consoleClient.placeSymbol(sessionId, userId, width, height) < 0) {
                        System.out.print("Введены неверные координаты!\nПопробуйте еще раз: ");
                        width = in.nextInt();
                        height = in.nextInt();
                    }
                }

                Thread.sleep(500);
            }

            System.out.print("Хотите сыграть снова?\nВведите 1, если да и 0 иначе: ");
            int replay = in.nextInt();
            if (replay != 1) {
                break replaygameloop;
            }
        }

        System.out.print("Хорошего дня!");
        in.close();
    }
}
