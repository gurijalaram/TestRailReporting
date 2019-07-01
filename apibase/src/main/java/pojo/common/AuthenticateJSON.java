package main.java.pojo.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import main.java.enums.Schema;
import main.java.json.deserializers.DateTimeDeserializer_yyyyMMddTHHmmss;

import java.time.LocalDateTime;

/**
 * @author kpatel
 */

//https://www.jsonschema.net/
@Schema(location = "AuthenticateJSONSchema.json")
public class AuthenticateJSON {

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("expires_in")
//    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmss.class)
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
