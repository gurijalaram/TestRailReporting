package com.apriori.utils.users;

public class UserCredentials {

    private String email;

    private String password;

    private String token;

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

    public String getPassword() {
        return password;
    }

    public UserCredentials setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getToken() {
        return token;
    }

    public UserCredentials setToken(String token) {
        this.token = token;
        return this;
    }
}
