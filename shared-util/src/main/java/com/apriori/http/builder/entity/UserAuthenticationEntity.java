package com.apriori.http.builder.entity;

/**
 * The class is intended to hold necessary user data to be able to login with a user through internal API.
 * You can also pass an already existing JSESSIONID from an already logged in user.
 */
public class UserAuthenticationEntity {
    private String emailAddress;
    private String password;
    private String sessionId;
    private String grantType;
    private String clientId;
    private String clientSecret;
    private String token;
    private String cloudContext;
    private String scope;
    private boolean alreadyLoggedIn;

    public UserAuthenticationEntity(String emailAddress, String password) {
        this.emailAddress = emailAddress;
        this.password = password;
        this.alreadyLoggedIn = false;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public UserAuthenticationEntity setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public UserAuthenticationEntity setPassword(String password) {
        this.password = password;
        return this;
    }

    public boolean getAlreadyLoggedIn() {
        return alreadyLoggedIn;
    }

    public String getSessionId() {
        return sessionId;
    }

    public UserAuthenticationEntity setSessionId(String sessionId) {
        this.sessionId = sessionId;
        return this;
    }

    public String getGrantType() {
        return grantType;
    }

    public UserAuthenticationEntity setGrantType(String grantType) {
        this.grantType = grantType;
        return this;
    }

    public String getClientId() {
        return clientId;
    }

    public UserAuthenticationEntity setClientId(String clientId) {
        this.clientId = clientId;
        return this;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public UserAuthenticationEntity setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
        return this;
    }

    public String getScope() {
        return scope;
    }

    public UserAuthenticationEntity setScope(String scope) {
        this.scope = scope;
        return this;
    }

    public boolean isAlreadyLoggedIn() {
        return alreadyLoggedIn;
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

    public String getToken() {
        return token;
    }

    public UserAuthenticationEntity setToken(String token) {
        this.token = token;
        return this;
    }

    @Override
    public int hashCode() {
        int result = emailAddress.hashCode();
        result = 31 * result + password.hashCode();
        return result;
    }
}
