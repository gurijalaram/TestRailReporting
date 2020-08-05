package com.apriori.nts.entity.response;

public class Credentials {
    private String username;
    private String password;
    private String host;

    public String getUsername() {
        return username;
    }

    public Credentials setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public Credentials setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getHost() {
        return host;
    }

    public Credentials setHost(String host) {
        this.host = host;
        return this;
    }
}
