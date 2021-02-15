package com.apriori.apibase.services.cas;

public class AttributeMappings {
    private String user_id;
    private String name;
    private String given_name;
    private String family_name;
    private String email;

    public String getUser_id() {
        return user_id;
    }

    public AttributeMappings setUser_id(String user_id) {
        this.user_id = user_id;
        return this;
    }

    public String getName() {
        return name;
    }

    public AttributeMappings setName(String name) {
        this.name = name;
        return this;
    }

    public String getGiven_name() {
        return given_name;
    }

    public AttributeMappings setGiven_name(String given_name) {
        this.given_name = given_name;
        return this;
    }

    public String getFamily_name() {
        return family_name;
    }

    public AttributeMappings setFamily_name(String family_name) {
        this.family_name = family_name;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public AttributeMappings setEmail(String email) {
        this.email = email;
        return this;
    }
}
