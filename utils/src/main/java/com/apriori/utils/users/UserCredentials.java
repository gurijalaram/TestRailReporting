package com.apriori.utils.users;

public class UserCredentials {

    private String username;

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

    public UserCredentials(String username, String password, String accessLevel) {
        this.username = username;
        this.password = password;
        this.accessLevel = accessLevel;
    }

    public UserCredentials(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getAccessLevel() {
        return accessLevel;
    }

    public UserCredentials setAccessLevel(String securityLevel) {
        this.accessLevel = securityLevel;
        return this;
    }

    public String getUsername() {
        return username;
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
        return token;
    }

    public UserCredentials setToken(String token) {
        this.token = token;
        return this;
    }
}
