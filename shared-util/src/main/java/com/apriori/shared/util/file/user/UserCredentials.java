package com.apriori.shared.util.file.user;

import com.apriori.shared.util.models.AuthorizationUtil;
import com.apriori.shared.util.models.CustomerUtil;

import com.auth0.jwt.JWT;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

public class UserCredentials implements Serializable {
    private static final int TOKEN_MIN_TIME_IN_MINUTES = 10;
    private volatile String token;
    @Getter
    private String email;
    @Getter
    private String password;
    private String username;
    @Getter
    private String cloudContext;
    //TODO : change it on Security ENUM when will be information about security levels
    @Getter
    private String accessLevel;
    private String userIdentity;

    public UserCredentials(String email, String password, String accessLevel) {
        this.email = email;
        this.password = password;
        this.accessLevel = accessLevel;
        this.userIdentity = getUserIdentity();
    }

    public UserCredentials(String email, String password) {
        this.email = email;
        this.password = password;
        this.userIdentity = getUserIdentity();
    }

    public UserCredentials() {
    }

    public static UserCredentials init(String username, String password) {
        return new UserCredentials(username, password);
    }

    public static UserCredentials initWithAccessLevel(String username, String password, String accessLevel) {
        return new UserCredentials(username, password, accessLevel);
    }

    public UserCredentials setAccessLevel(String securityLevel) {
        this.accessLevel = securityLevel;
        return this;
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

    public UserCredentials setPassword(String password) {
        this.password = password;
        return this;
    }

    public synchronized String getToken() {
        if (token == null) {
            generateToken();
        }
        if (ChronoUnit.MINUTES.between(LocalTime.now(),
            Instant.ofEpochMilli(new JWT().decodeJwt(token).getExpiresAt().getTime())
                .atZone(ZoneId.systemDefault()).toLocalTime()) <= TOKEN_MIN_TIME_IN_MINUTES) {
            generateToken();
        }
        return token;
    }

    public UserCredentials generateToken() {
        this.token = new AuthorizationUtil().getToken(this)
            .getResponseEntity()
            .getToken();
        return this;
    }

    public UserCredentials generateCloudContext() {
        this.cloudContext = cloudContext != null ? cloudContext : CustomerUtil.getAuthTargetCloudContext(this);
        return this;
    }

    public synchronized String getUserIdentity() {
        return userIdentity != null ? userIdentity : UserUtil.getUserByEmail(this).getIdentity();
    }
}
