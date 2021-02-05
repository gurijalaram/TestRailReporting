package com.apriori.cds.entity.response.apVersion;

import com.apriori.utils.http.enums.Schema;

@Schema(location = "cds/apVersionsSchema.json")
public class ApVersionItem {

    private String identity;
    private String createdBy;
    private String createdAt;
    private String version;

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

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
