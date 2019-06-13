package main.java.pojo.common;

import main.java.enums.Schema;

/**
 * @author kpatel
 */
@Schema(location = "commonschemas/login.json")
public class LoginJSON {

    private String sessionId;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
