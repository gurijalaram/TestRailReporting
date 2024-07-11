package com.apriori.shared.util.file.user;

import com.apriori.shared.util.AuthorizationUtil;
import com.apriori.shared.util.SharedCustomerUtil;
import com.apriori.shared.util.enums.RolesEnum;
import com.apriori.shared.util.models.response.User;

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
    private RolesEnum role;
    private User user;

    public UserCredentials(String email, String password, RolesEnum role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public UserCredentials(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public UserCredentials() {
    }

    /**
     * Initialize user
     *
     * @param username - the username
     * @param password - the password
     * @return current object
     */
    public static UserCredentials init(String username, String password) {
        return new UserCredentials(username, password);
    }

    /**
     * Initialize user with role
     *
     * @param username - the username
     * @param password - the password
     * @param role     - the role
     * @return current object
     */
    public static UserCredentials initWithRole(String username, String password, RolesEnum role) {
        return new UserCredentials(username, password, role);
    }

    /**
     * Set security level
     *
     * @param securityLevel - the security level
     * @return current object
     */
    public UserCredentials setRole(RolesEnum securityLevel) {
        this.role = securityLevel;
        return this;
    }

    /**
     * Get username
     *
     * @return string
     */
    public String getUsername() {
        return email.split("@")[0];
    }

    /**
     * Get token
     *
     * @return string
     */
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

    /**
     * Generate token
     *
     * @return current object
     */
    public UserCredentials generateToken() {
        this.token = new AuthorizationUtil().getToken(this)
            .getResponseEntity()
            .getToken();
        return this;
    }

    /**
     * Generates cloud context
     *
     * @return current object
     */
    // TODO: 20/06/2024 cn - this should not be in this class as this is a customer (eg. apriori-internal) construct or make customer a sub-object of user
    public UserCredentials generateCloudContext() {
        this.cloudContext = cloudContext != null ? cloudContext : SharedCustomerUtil.getAuthTargetCloudContext();
        return this;
    }

    /**
     * Get user details
     *
     * @return new object
     */
    public synchronized User getUserDetails() {
        return user != null ? user : UserUtil.getUserByEmail(this);
    }
}
