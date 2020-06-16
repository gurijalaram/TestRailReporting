package com.apriori.ats.entity.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Claims {
    @JsonProperty
    private String name;

    @JsonProperty
    private String email;

    public String getEmail() {
        return email;
    }

    public Claims setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getName() {
        return name;
    }

    public Claims setName(String name) {
        this.name = name;
        return this;
    }
}
