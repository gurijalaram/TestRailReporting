package com.apriori.cds.entity.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Site {
    @JsonProperty
    private String identity;
    @JsonProperty
    private String createdBy;
    @JsonProperty
    private String createdAt;
    @JsonProperty
    private String siteIdentity;

    public Site setIdentity(String identity) {
        this.identity = identity;
        return this;
    }

    public String getIdentity() {
        return this.identity;
    }

    public Site setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public Site setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public String getCreatedAt() {
        return this.createdAt;
    }

    public Site setSiteIdentity(String siteIdentity) {
        this.siteIdentity = siteIdentity;
        return this;
    }

    public String getSiteIdentity() {
        return this.siteIdentity;
    }

}
