package com.apriori.cds.objects.request;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class License {
    private String description;
    private String apVersion;
    private String createdBy;
    private String active;
    private String license;
    private String licenseTemplate;

    public String getDescription() {
        return description;
    }

    public License setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getApVersion() {
        return apVersion;
    }

    public License setApVersion(String apVersion) {
        this.apVersion = apVersion;
        return this;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public License setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public String getActive() {
        return active;
    }

    public License setActive(String active) {
        this.active = active;
        return this;
    }

    public String getLicense() {
        return license;
    }

    public License setLicense(String license) {
        this.license = license;
        return this;
    }

    public String getLicenseTemplate() {
        return licenseTemplate;
    }

    public License setLicenseTemplate(String licenseTemplate) {
        this.licenseTemplate = licenseTemplate;
        return this;
    }
}
