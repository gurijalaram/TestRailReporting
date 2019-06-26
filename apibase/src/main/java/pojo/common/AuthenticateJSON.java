package main.java.pojo.common;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import main.java.enums.Schema;
import main.java.json.deserializers.DateTimeDeserializer_yyyyMMddTHHmmss;

import java.time.LocalDateTime;

/**
 * @author kpatel
 */
@Schema(location = "AuthenticateJSONSchema.json")
public class AuthenticateJSON {

    private String auth;
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmss.class)
    private LocalDateTime expires;
    private String status;

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getExpires() {
        return expires;
    }

    public void setExpires(LocalDateTime expires) {
        this.expires = expires;
    }
}
