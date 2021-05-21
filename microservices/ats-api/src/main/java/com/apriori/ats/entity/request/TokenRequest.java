package com.apriori.ats.entity.request;

import com.apriori.ats.entity.response.TokenInformation;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TokenRequest {
    @JsonProperty
    private TokenInformation token;

    public TokenRequest setToken(TokenInformation token) {
        this.token = token;
        return this;
    }

    public TokenInformation getToken() {
        return this.token;
    }
    

}

