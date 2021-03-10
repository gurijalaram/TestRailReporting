package com.apriori.cds.objects.response;

import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "cds/SiteSchema.json")
public class LicensedApplication {
    private String identity;
    private String createdBy;
    private String createdAt;
    private String application;
    private String applicationIdentity;

    public String getIdentity() {
        return identity;
    }

    public LicensedApplication setIdentity(String identity) {
        this.identity = identity;
        return this;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public LicensedApplication setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public LicensedApplication setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public String getApplication() {
        return application;
    }

    public LicensedApplication setApplication(String application) {
        this.application = application;
        return this;
    }

    public String getApplicationIdentity() {
        return applicationIdentity;
    }

    public LicensedApplication setApplicationIdentity(String applicationIdentity) {
        this.applicationIdentity = applicationIdentity;
        return this;
    }
}