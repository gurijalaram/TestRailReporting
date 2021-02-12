package com.apriori.cds.entity.response;

import com.apriori.utils.http.enums.Schema;

@Schema(location = "cds/ConfigurationSchema.json")
public class ConfigurationItems {
    private String identity;
    private String createdBy;
    private String createdAt;
    private String domain;

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }
}