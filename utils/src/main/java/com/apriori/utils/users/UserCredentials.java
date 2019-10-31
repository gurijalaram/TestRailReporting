package com.apriori.utils.users;

public class UserCredentials {

    private String username;

    private String password;

    //TODO z: change it on Security ENUM when will be information about security levels
    private String securityLevel;

    public UserCredentials(String username, String password, String securityLevel) {
        this.username = username;
        this.password = password;
        this.securityLevel = securityLevel;
    }

    public UserCredentials(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getSecurityLevel() {
        return securityLevel;
    }

    public UserCredentials setSecurityLevel(String securityLevel) {
        this.securityLevel = securityLevel;
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
}
