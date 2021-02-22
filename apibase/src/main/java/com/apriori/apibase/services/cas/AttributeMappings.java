package com.apriori.apibase.services.cas;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AttributeMappings {
    @JsonProperty("user_id")
    private String user_id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("given_name")
    private String given_name;
    @JsonProperty("family_name")
    private String family_name;
    @JsonProperty("email")
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
