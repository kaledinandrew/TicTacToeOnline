package org.TicTacToeCLI;

import static org.TicTacToeCLI.ServiceUrls.*;
import static org.TicTacToeCLI.Connection.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Objects;

public class UserClient extends SessionObserver {
    public UserClient(ServiceUrls urls) {
        super(urls);
    }

    public String createUser(String name, int symbol) {
        HttpURLConnection connection = connect(super.urls.getUsersLink()+"?name="+name+"&symbol="+symbol);

        if (Objects.nonNull(connection)) {
            return this.postUpdate(connection);
        } else {
            System.out.println("Не удалось создать пользователя");
            return "";
        }
    }

    public String getUserInfo(int userId) {
        URL url = null;
        try {
            url = new URL(urls.getUsersLink() + "?userId=" + String.valueOf(userId));
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
