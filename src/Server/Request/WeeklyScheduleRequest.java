package Server.Request;

import Server.SessionToken;

import java.io.Serializable;

public class WeeklyScheduleRequest implements Serializable {
    private SessionToken sessionToken;
    public WeeklyScheduleRequest(SessionToken sessionToken){
        this.sessionToken = sessionToken;
    }

    public SessionToken getSessionToken() {
        return sessionToken;
    }
}