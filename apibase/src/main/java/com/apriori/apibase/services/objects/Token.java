package com.apriori.apibase.services.objects;

import com.apriori.apibase.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

@Schema(location = "AtsTokenSchema.json")
public class Token {
    @JsonProperty
    private String token;

    public String getToken() {
        return this.token;
    }

}
