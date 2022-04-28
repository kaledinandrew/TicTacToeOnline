package org.TicTacToeCLI;

import org.TicTacToeCLI.*;
import static org.TicTacToeCLI.Variables.*;
import static org.TicTacToeCLI.Connection.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Map;
import java.util.HashMap;
import java.util.Objects;

public class UserClient implements SessionObserver {

    @Override // OK
    public String getUpdate(HttpURLConnection connection) {

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
    public String postUpdate(HttpURLConnection connection, String jsonInputString) {

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

        return answ;
    }

    @Override // OK
    public String putUpdate(HttpURLConnection connection, String jsonInputString) {
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
    public String createUser(String name, int symbol) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", name);
        parameters.put("symbol", symbol);
        String jsonInputString = JsonParser.MapToString(parameters);

        HttpURLConnection connection = connect(USERS_LINK);

        if (Objects.nonNull(connection)) {
            return this.postUpdate(connection, jsonInputString);
        } else {
            System.out.println("Не удалось создать пользователя");
            return "";
        }
    }

    public String getUserInfo(int userId) {
        URL url = null;
        try {
            url = new URL(USERS_LINK + "?userId=" + String.valueOf(userId));
        } catch (MalformedURLException e) {
            e.printStackTrace();
            System.out.println("Пользователь не найден");
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


    // POST/PATCH
    // public void updateUser() {}
    // DELETE
    // public void deleteUser() {}
}
