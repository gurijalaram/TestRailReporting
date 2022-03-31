package com.apriori.utils.reader.file.user;

import com.apriori.utils.authorization.AuthorizationUtil;
import com.apriori.utils.json.utils.JsonManager;

import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;

public class UserCredentials {

    private String email;
    private String password;
    private String token;
    private String username;
    private String cloudContext;
    private final int TIME_TO_GET_TOKEN = 10;

    public static UserCredentials init(String username, String password) {
        return new UserCredentials(username, password);
    }

    public static UserCredentials initWithAccessLevel(String username, String password, String accessLevel) {
        return new UserCredentials(username, password, accessLevel);
    }

    //TODO z: change it on Security ENUM when will be information about security levels
    private String accessLevel;

    public UserCredentials(String email, String password, String accessLevel) {
        this.email = email;
        this.password = password;
        this.accessLevel = accessLevel;
    }

    public UserCredentials(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getAccessLevel() {
        return accessLevel;
    }

    public UserCredentials setAccessLevel(String securityLevel) {
        this.accessLevel = securityLevel;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserCredentials setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getUsername() {
        return email.split("@")[0];
    }

    public UserCredentials setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public UserCredentials setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getToken() {
        if (tokenTimeRemain() <= TIME_TO_GET_TOKEN || token == null) {
            generateToken();
        }
        return token;
    }

    public UserCredentials generateToken() {
        this.token = new AuthorizationUtil(this).getToken()
            .getResponseEntity()
            .getToken();
        return this;
    }

    public String getCloudContext() {
        return cloudContext;
    }

    public UserCredentials generateCloudContext() {
        this.cloudContext = cloudContext != null ? cloudContext : new AuthorizationUtil().getAuthTargetCloudContext(this);
        return this;
    }

    private long tokenTimeRemain() {
        String[] tokens = token.split("\\.");

        Base64.Decoder decoder = Base64.getUrlDecoder();

        String payload = new String(decoder.decode(tokens[1]));

        TokenInfo tokenEntity = JsonManager.deserializeJsonFromString(payload, TokenInfo.class);

        Date expireAt = Date.from(Instant.ofEpochSecond(Integer.parseInt(tokenEntity.getExpireAt())));

        return ChronoUnit.MINUTES.between(LocalTime.now(), Instant.ofEpochMilli(expireAt.getTime()).atZone(ZoneId.systemDefault()).toLocalTime());
    }
}
