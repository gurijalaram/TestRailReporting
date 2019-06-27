package main.java.pojo.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import main.java.enums.Schema;
import main.java.json.deserializers.DateTimeDeserializer_yyyyMMddTHHmmss;

import java.time.LocalDateTime;

/**
 * @author kpatel
 */
@Schema(location = "AuthenticateJSONSchema.json")
public class AuthenticateJSON {

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("expires_in")
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmss.class)
    private LocalDateTime expires_in;

    public String getAccessToken() {
        return accessToken;
    }

    public AuthenticateJSON setAccessToken(String accessToken) {
        this.accessToken = accessToken;
        return this;
    }

    public LocalDateTime getExpires_in() {
        return expires_in;
    }

    public AuthenticateJSON setExpires_in(LocalDateTime expires_in) {
        this.expires_in = expires_in;
        return this;
    }
}
