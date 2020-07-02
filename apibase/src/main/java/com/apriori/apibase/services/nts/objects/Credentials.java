package com.apriori.apibase.services.nts.objects;

public class Credentials {
    private String username;
    private String password;
    private String address;

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

    public String getAddress() {
        return address;
    }

    public Credentials setAddress(String address) {
        this.address = address;
        return this;
    }
}
