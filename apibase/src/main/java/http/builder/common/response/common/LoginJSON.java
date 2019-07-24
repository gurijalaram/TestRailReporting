package main.java.http.builder.common.response.common;

import main.java.http.enums.Schema;

/**
 * @author kpatel
 */
@Schema(location = "LoginJSONSchema.json")
public class LoginJSON {

    private String sessionId;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
