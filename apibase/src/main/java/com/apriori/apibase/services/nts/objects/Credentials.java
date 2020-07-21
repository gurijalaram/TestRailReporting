package com.apriori.apibase.services.nts.objects;

public class Credentials {
    private String username;
    private String password;
    private String Host;

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
        return Host;
    }

    public Credentials setHost(String Host) {
        this.Host = Host;
        return this;
    }
}
