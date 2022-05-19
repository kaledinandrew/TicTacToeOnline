package org.TicTacToeCLI;

import static org.TicTacToeCLI.Variables.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Objects;

public class Console implements SessionObserver {

    @Override // OK
    public String getUpdate (HttpURLConnection connection) {

        try {
            connection.setRequestMethod("GET");
        } catch (ProtocolException e) {
            e.printStackTrace();
            System.out.println("Невозможно создать GET-запрос");
            System.exit(1);
        }
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setConnectTimeout(CONNECTION_TIMEOUT);
        connection.setReadTimeout(CONNECTION_TIMEOUT);

        InputStreamReader reader = null;
        try {
            reader = new InputStreamReader(connection.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Ошибка чтения запроса в InputStreamReader");
            return "";
        }

        try (BufferedReader in = new BufferedReader(reader)) {
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            return content.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Ошибка чтения запроса в BufferedReader");
            return "";
        }
    }

    @Override // OK
    public String postUpdate (HttpURLConnection connection, String jsonInputString) {

        try {
            connection.setRequestMethod("POST");
        } catch (ProtocolException e) {
            e.printStackTrace();
            System.out.println("Невозможно создать POST-запрос");
            System.exit(1);
        }

        connection.setRequestProperty("Content-Type", "application/json; utf-8");
        connection.setRequestProperty("Accept", "application/json");
        connection.setDoOutput(true);
        connection.setConnectTimeout(CONNECTION_TIMEOUT);
        connection.setReadTimeout(READ_TIMEOUT);

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("UTF-8");
            os.write(input, 0, input.length);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Не удалось записать JSON");
            System.exit(1);
        }

        int code = 0;
        try {
            code = connection.getResponseCode();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Не удалось установить ответ соединения");
            System.exit(1);
        }

        if (HttpURLConnection.HTTP_OK != code) {
            System.out.printf("Failed %d\n", code);
            System.exit(1);
        }

        String answ = "";
        try (
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(connection.getInputStream(), "utf-8"))
        ) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }

            answ = response.toString();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Не удалось получить ответ");
            System.exit(1);
        }

        return answ;
    }

    @Override // OK
    public String putUpdate (HttpURLConnection connection, String jsonInputString) {
        try {
            connection.setRequestMethod("PUT");
        } catch (ProtocolException e) {
            e.printStackTrace();
            System.out.println("Невозможно создать POST-запрос");
            System.exit(1);
        }

        connection.setRequestProperty("Content-Type", "application/json; utf-8");
        connection.setRequestProperty("Accept", "application/json");
        connection.setDoOutput(true);
        connection.setConnectTimeout(CONNECTION_TIMEOUT);
        connection.setReadTimeout(READ_TIMEOUT);

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("UTF-8");
            os.write(input, 0, input.length);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Не удалось записать JSON");
            System.exit(1);
        }

        int code = 0;
        try {
            code = connection.getResponseCode();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Не удалось установить ответ соединения");
            System.exit(1);
        }

        if (HttpURLConnection.HTTP_OK != code) {
            System.out.printf("Failed %d\n", code);
            System.exit(1);
        }

        String answ = "";
        try (
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(connection.getInputStream(), "utf-8"))
        ) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }

            answ = response.toString();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Не удалось получить ответ");
            System.exit(1);
        }

        return answ;
    }

    // POST -- OK
    public String startSession (int hostId) {
        Map<String, Object> parameters  = new HashMap<>();
        parameters.put("hostId", hostId);
        String jsonInputString = JsonParser.MapToString(parameters);

        HttpURLConnection connection = Connection.connect(SESSIONS_LINK + "?hostId=" + String.valueOf(hostId));
        if (Objects.nonNull(connection)) {
            return this.postUpdate(connection, jsonInputString);
        } else {
            System.out.println("Не удалось создать сессию");
            return "";
        }
    }

    // PUT -- OK
    public String connectToSession (int sessionId, int guestId) {
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

    // PUT -- OK
    public String placeSymbol (int sessionId, int userId, int x, int y) throws IOException {
        // check if x, y are valid
        if (x < 0 || y < 0) { throw new IOException("Невалидные x, y"); }

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
            return this.putUpdate(connection, jsonInputString);
        } else {
            System.out.println("Не удалось поставить символ");
            return "";
        }
    }

    // GET -- OK
    public void showField (int sessionId) {

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
//        var connection = Connection.connect(SERVER_LINK + "/field"); // exmpl
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
