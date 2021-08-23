package com.apriori.apibase.services.ats.objects;

import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

@Schema(location = "common/AtsTokenSchema.json")
public class Token {
    @JsonProperty
    private String token;

    public String getToken() {
        return this.token;
    }

    public Token setToken(String token) {
        this.token = token;
        return this;
    }
}
