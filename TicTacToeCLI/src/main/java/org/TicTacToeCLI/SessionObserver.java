package org.TicTacToeCLI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.nio.charset.StandardCharsets;

import static org.TicTacToeCLI.Variables.CONNECTION_TIMEOUT;
import static org.TicTacToeCLI.Variables.READ_TIMEOUT;

public class SessionObserver {
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

        InputStreamReader reader;
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
    public String postUpdate(HttpURLConnection connection) {

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
                        new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))
        ) {
            StringBuilder response = new StringBuilder();
            String responseLine;
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
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
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
                        new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))
        ) {
            StringBuilder response = new StringBuilder();
            String responseLine;
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
}
