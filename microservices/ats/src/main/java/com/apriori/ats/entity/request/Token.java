package com.apriori.ats.entity.request;

import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

@Schema(location = "AtsTokenSchema.json")
public class Token {
    @JsonProperty
    private String token;

    public String getToken() {
        return this.token;
    }

}
