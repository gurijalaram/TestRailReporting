package com.apriori.ats.entity.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthorizeRequest {
    @JsonProperty
    private String application;

    @JsonProperty
    private String targetCloudContext;

    @JsonProperty
    private String token;

    public AuthorizeRequest setApplication(String application) {
        this.application = application;
        return this;
    }

    public AuthorizeRequest setTargetCloudContext(String targetCloudContext) {
        this.targetCloudContext = targetCloudContext;
        return this;
    }

    public AuthorizeRequest setToken(String token) {
        this.token = token;
        return this;
    }
}
