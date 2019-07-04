package main.java.http.builder.common.entity;

import main.java.enums.UsersEnum;

/**
 * The class is intended to hold necessary user data to be able to login with a user through internal API.
 * You can also pass an already existing JSESSIONID from an already logged in user.
 */
public class UserAuthenticationEntity {
    private String emailAddress;
    private String password;
    private String sessionId;
    private String grant_type;
    private String client_id;
    private String client_secret;
    private String scope;
    private boolean alreadyLoggedIn;

    public UserAuthenticationEntity(){}

    public UserAuthenticationEntity(String emailAddress, String password) {
        this.emailAddress = emailAddress;
        this.password = password;
        this.alreadyLoggedIn = false;
    }

    public UserAuthenticationEntity(UsersEnum usersEnum) {
        this.emailAddress = usersEnum.getUsername();
        this.password = usersEnum.getPassword();
        this.alreadyLoggedIn = false;
    }

    public UserAuthenticationEntity(String emailAddress, String sessionId, boolean alreadyLoggedIn) {
        this.emailAddress = emailAddress;
        this.sessionId = sessionId;
        this.alreadyLoggedIn = alreadyLoggedIn;
    }

    public UserAuthenticationEntity(String emailAddress, String password, String sessionId, String grant_type, String client_id, String client_secret, String scope, boolean alreadyLoggedIn) {
        this.emailAddress = emailAddress;
        this.password = password;
        this.sessionId = sessionId;
        this.grant_type = grant_type;
        this.client_id = client_id;
        this.client_secret = client_secret;
        this.scope = scope;
        this.alreadyLoggedIn = alreadyLoggedIn;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getPassword() {
        return password;
    }

    public boolean getAlreadyLoggedIn() {
        return alreadyLoggedIn;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getGrant_type() {
        return grant_type;
    }

    public String getClient_id() {
        return client_id;
    }

    public String getClient_secret() {
        return client_secret;
    }

    public String getScope() {
        return scope;
    }

    public boolean isAlreadyLoggedIn() {
        return alreadyLoggedIn;
    }

    public UserAuthenticationEntity setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
        return this;
    }

    public UserAuthenticationEntity setPassword(String password) {
        this.password = password;
        return this;
    }

    public UserAuthenticationEntity setSessionId(String sessionId) {
        this.sessionId = sessionId;
        return this;
    }

    public UserAuthenticationEntity setGrant_type(String grant_type) {
        this.grant_type = grant_type;
        return this;
    }

    public UserAuthenticationEntity setClient_id(String client_id) {
        this.client_id = client_id;
        return this;
    }

    public UserAuthenticationEntity setClient_secret(String client_secret) {
        this.client_secret = client_secret;
        return this;
    }

    public UserAuthenticationEntity setScope(String scope) {
        this.scope = scope;
        return this;
    }

    public UserAuthenticationEntity setAlreadyLoggedIn(boolean alreadyLoggedIn) {
        this.alreadyLoggedIn = alreadyLoggedIn;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        UserAuthenticationEntity that = (UserAuthenticationEntity) o;

        if (!emailAddress.equals(that.emailAddress)) {
            return false;
        }
        return password.equals(that.password);
    }

    @Override
    public int hashCode() {
        int result = emailAddress.hashCode();
        result = 31 * result + password.hashCode();
        return result;
    }
}
