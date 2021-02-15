package com.apriori.cds.objects.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Deployment {
    @JsonProperty
    private Boolean isDefault;
    @JsonProperty
    private String identity;
    @JsonProperty
    private String createdBy;
    @JsonProperty
    private String createdAt;
    @JsonProperty
    private String name;
    @JsonProperty
    private String description;
    @JsonProperty
    private String apVersion;
    @JsonProperty
    private Boolean active;
    @JsonProperty
    private List<Object> installations = null;
    @JsonProperty
    private String customerIdentity;
    @JsonProperty
    private List<Application> applications = null;
    @JsonProperty
    private String deploymentType;
    @JsonProperty
    private List<String> sites = null;

    public Boolean getIsDefault() {
        return isDefault;
    }

    public Deployment setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
        return this;
    }

    public String getIdentity() {
        return identity;
    }

    public Deployment setIdentity(String identity) {
        this.identity = identity;
        return this;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public Deployment setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public Deployment setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public String getName() {
        return name;
    }

    public Deployment setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Deployment setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getApVersion() {
        return apVersion;
    }

    public Deployment setApVersion(String apVersion) {
        this.apVersion = apVersion;
        return this;
    }

    public Boolean getActive() {
        return active;
    }

    public Deployment setActive(Boolean active) {
        this.active = active;
        return this;
    }

    public List<Object> getInstallations() {
        return installations;
    }

    public Deployment setInstallations(List<Object> installations) {
        this.installations = installations;
        return this;
    }

    public String getCustomerIdentity() {
        return customerIdentity;
    }

    public Deployment setCustomerIdentity(String customerIdentity) {
        this.customerIdentity = customerIdentity;
        return this;
    }

    public List<Application> getApplications() {
        return applications;
    }

    public Deployment setApplications(List<Application> applications) {
        this.applications = applications;
        return this;
    }

    public String getDeploymentType() {
        return deploymentType;
    }

    public Deployment setDeploymentType(String deploymentType) {
        this.deploymentType = deploymentType;
        return this;
    }

    public List<String> getSites() {
        return sites;
    }

    public Deployment setSites(List<String> sites) {
        this.sites = sites;
        return this;
    }

}
