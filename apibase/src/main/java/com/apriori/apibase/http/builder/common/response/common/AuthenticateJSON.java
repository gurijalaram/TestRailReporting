package com.apriori.apibase.http.builder.common.response.common;

import com.apriori.apibase.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author kpatel
 */

//https://www.jsonschema.net/
@Schema(location = "AuthenticateJSONSchema.json")
public class AuthenticateJSON {

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("expires_in")
    private Integer expiresIn;

    public String getAccessToken() {
        return accessToken;
    }

    public AuthenticateJSON setAccessToken(String accessToken) {
        this.accessToken = accessToken;
        return this;
    }

    public Integer getExpiresIn() {
        return expiresIn;
    }

    public AuthenticateJSON setExpiresIn(Integer expiresIn) {
        this.expiresIn = expiresIn;
        return this;
    }
}
