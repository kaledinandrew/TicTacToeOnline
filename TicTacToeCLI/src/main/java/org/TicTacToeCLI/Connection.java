package org.TicTacToeCLI;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Connection {

    public static HttpURLConnection connect(String link) {
        URL url = null;
        HttpURLConnection connection = null;

        try {
            url = new URL(link);
        } catch (MalformedURLException e) {
            System.out.println("Неопознанный URL");
            e.printStackTrace();
            System.exit(1);
        }

        try {
            connection = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            System.out.println("Не удалось поключиться к серверу");
            e.printStackTrace();
            System.exit(1);
        }

        return connection;
    }

}
