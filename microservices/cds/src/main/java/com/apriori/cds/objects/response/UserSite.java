package com.apriori.cds.objects.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserSite {
    @JsonProperty
    private String identity;
    @JsonProperty
    private String createdBy;
    @JsonProperty
    private String createdAt;
    @JsonProperty
    private String siteIdentity;

    public String getIdentity() {
        return identity;
    }

    public UserSite setIdentity(String identity) {
        this.identity = identity;
        return this;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public UserSite setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public UserSite setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public String getSiteIdentity() {
        return siteIdentity;
    }

    public UserSite setSiteIdentity(String siteIdentity) {
        this.siteIdentity = siteIdentity;
        return this;
    }
}
