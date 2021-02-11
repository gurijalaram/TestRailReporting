package com.apriori.apibase.services.cds.objects;

import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

@Schema(location = "cds/ApVersionSchema.json")
public class ApVersion {
    @JsonProperty
    private ApVersion response;
    @JsonProperty
    private String identity;
    @JsonProperty
    private String createdBy;
    @JsonProperty
    private String createdAt;
    @JsonProperty
    private String version;

    public ApVersion getResponse() {
        return response;
    }

    public ApVersion setResponse(ApVersion response) {
        this.response = response;
        return this;
    }

    public String getIdentity() {
        return identity;
    }

    public ApVersion setIdentity(String identity) {
        this.identity = identity;
        return this;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public ApVersion setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public ApVersion setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public String getVersion() {
        return version;
    }

    public ApVersion setVersion(String version) {
        this.version = version;
        return this;
    }
}
