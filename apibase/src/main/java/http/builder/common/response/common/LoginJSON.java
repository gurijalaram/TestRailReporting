package main.java.http.builder.common.response.common;

import main.java.http.enums.Schema;

/**
 * @author kpatel
 */
// TODO: replace location of json schema with actual location
@Schema(location = "location_of_login_json")
public class LoginJSON {

    private String sessionId;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
