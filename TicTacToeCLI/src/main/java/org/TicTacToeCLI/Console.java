package org.TicTacToeCLI;

import static org.TicTacToeCLI.Variables.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Objects;

public class Console extends SessionObserver {

    public String startSession(int hostId) {
        HttpURLConnection connection = Connection.connect(SESSIONS_LINK + "?hostId=" + String.valueOf(hostId));
        if (Objects.nonNull(connection)) {
            return this.postUpdate(connection);
        } else {
            System.out.println("Не удалось создать сессию");
            return "";
        }
    }

    public String connectToSession(int sessionId, int guestId) {
        Map<String, Object> parameters  = new HashMap<>();
        parameters.put("sessionId", sessionId);
        parameters.put("guestId", guestId);
        String jsonInputString = JsonParser.MapToString(parameters);

        HttpURLConnection connection = Connection.connect(SESSIONS_CONNECT_LINK +
                "/?sessionId=" + String.valueOf(sessionId) + "&guestId=" + String.valueOf(guestId));

        if (Objects.nonNull(connection)) {
            return this.putUpdate(connection, jsonInputString);
        } else {
            System.out.println("Не удалось подключиться к сесси");
            return "";
        }
    }

    public int placeSymbol(int sessionId, int userId, int x, int y) throws IOException {
        Map<String, Object> parameters  = new HashMap<>();
        parameters.put("sessionId", sessionId);
        parameters.put("userId", userId);
        parameters.put("x", x);
        parameters.put("y", y);
        String jsonInputString = JsonParser.MapToString(parameters);

        HttpURLConnection connection = Connection.connect(PLACE_SYMBOL_LINK +
                "/?sessionId=" + String.valueOf(sessionId) + "&userId=" + String.valueOf(userId) +
                "&x=" + String.valueOf(x) + "&y=" + String.valueOf(y));

        if (Objects.nonNull(connection)) {
            // return this.putUpdate(connection, jsonInputString);
            return 1;
        } else {
            System.out.println("Не удалось поставить символ");
            // return "";
            return -1;
        }
    }

    public void showField(int sessionId) {

        URL url = null;
        try {
            url = new URL(SESSIONS_LINK + "?sessionId=" + String.valueOf(sessionId));
        } catch (MalformedURLException e) {
            e.printStackTrace();
            System.out.println("Сессия не найдена");
            System.exit(1);
        }

        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Не удалось открыть соединение");
            System.exit(1);
        }

        String output = this.getUpdate(connection);

        Map<String, Object> mp = JsonParser.parseJSON(output);
        String mat = mp.get("field").toString();
        ArrayList<ArrayList<Integer>> matrix = JsonParser.parseField(mat);
        int m = matrix.size();
        int n = matrix.get(0).size();

        {
            for (int i = 0; i < n; i++) {
                System.out.printf("\t%d", i);
            }

            System.out.println();

            for (int i = 0; i < m; i++) {
                for (int j = -1; j < n; j++) {
                    if (j == -1) {
                        System.out.printf("%d", i);
                    } else {
                        System.out.printf("\t%d", matrix.get(i).get(j));
                    }
                }
                System.out.println();
            }
        }
    }

    public void showField (String jsonString) {

        String mat = JsonParser.getField(jsonString);
        ArrayList<ArrayList<Integer>> matrix = JsonParser.parseField(mat);
        int m = matrix.size();
        int n = matrix.get(0).size();

        {
            for (int i = 0; i < n; i++) {
                System.out.printf("\t%d", i);
            }

            System.out.println();

            for (int i = 0; i < m; i++) {
                for (int j = -1; j < n; j++) {
                    if (j == -1) {
                        System.out.printf("%d", i);
                    } else {
                        System.out.printf("\t%d", matrix.get(i).get(j));
                    }
                }
                System.out.println();
            }
        }
    }

    // GET
//    public String showResult() throws Exception {
//        var connection = Connection.connect(SERVER_LINK + "/field");
//        var output = this.getUpdate(connection);
//        // ... -- parse string
//        String result = "";
//        if (result != "") {
//            System.out.println("You " + result + "!");
//        }
//
//        return result;
//    }

    // public void updateField() throws Exception {} // ???

    public String getSessionInfo(int sessionId) {
        URL url = null;
        try {
            url = new URL(SESSIONS_LINK + "?sessionId=" + String.valueOf(sessionId));
        } catch (MalformedURLException e) {
            e.printStackTrace();
            System.out.println("Сессия не найден");
            System.exit(1);
        }

        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Не удалось открыть соединение");
            System.exit(1);
        }

        return this.getUpdate(connection);
    }
}
