package fr.pagelib.termapp.wsc;

public class Session {

    private String sessionID;
    private String userID;

    public Session (String sessionID, String userID) {
        this.sessionID = sessionID;
        this.userID = userID;
    }

    public String getSessionID() {
        return sessionID;
    }

    public String getUserID() {
        return userID;
    }
}
