package com.apriori.cds.entity.response;

import com.apriori.utils.http.enums.Schema;

@Schema(location = "ConfigurationSchema.json")
public class ConfigurationItems {
    private String identity;
    private String createdBy;
    private String createdAt;
    private String domain;

    public String getIdentity() {
        return identity;
    }

    public ConfigurationItems setIdentity(String identity) {
        this.identity = identity;
        return this;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public ConfigurationItems setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public ConfigurationItems setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public String getDomain() {
        return domain;
    }

    public ConfigurationItems setDomain(String domain) {
        this.domain = domain;
        return this;
    }
}