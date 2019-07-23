package main.java.http.builder.common.response.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import main.java.http.enums.Schema;

/**
 * @author kpatel
 */

//https://www.jsonschema.net/
@Schema(location = "AuthenticateJSONSchema.json")
public class AuthenticateJSON {

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("expires_in")
    private Integer expiresIn;

    public String getAccessToken() {
        return accessToken;
    }

    public AuthenticateJSON setAccessToken(String accessToken) {
        this.accessToken = accessToken;
        return this;
    }

    public Integer getExpiresIn() {
        return expiresIn;
    }

    public AuthenticateJSON setExpiresIn(Integer expiresIn) {
        this.expiresIn = expiresIn;
        return this;
    }
}
