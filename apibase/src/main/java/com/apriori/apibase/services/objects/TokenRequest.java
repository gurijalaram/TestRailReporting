package com.apriori.apibase.services.objects;

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

