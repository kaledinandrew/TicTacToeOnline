package org.TicTacToeCLI;

public class ServiceUrls {
    private final String baseUrl;
    private final String usersLink;
    private final String sessionsLink;
    private final String sessionsConnectLink;
    private final String placeSymbolLink;

    public ServiceUrls(String baseUrl) {
        this.baseUrl = baseUrl;
        this.usersLink = baseUrl+"/users";
        this.sessionsLink = baseUrl+"/sessions";
        this.sessionsConnectLink = this.sessionsLink+"/connect";
        this.placeSymbolLink = this.sessionsLink+"place-symbol";
    }

    public String getServerLink() {
        return baseUrl;
    }

    public String getUsersLink() {
        return usersLink;
    }

    public String getSessionsLink() {
        return sessionsLink;
    }

    public String getSessionsConnectLink() {
        return sessionsConnectLink;
    }

    public String getPlaceSymbolLink() {
        return placeSymbolLink;
    }

    public int getConnectionTimeout() {
        return 10000;
    }

    public int getReadTimeout() {
        return 5000;
    }
}
