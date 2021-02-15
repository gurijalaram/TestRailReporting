package com.apriori.cds.entity.request;

import com.apriori.utils.http.enums.Schema;

import java.util.List;

@Schema(location = "cds/AddDeploymentSchema.json")
public class AddDeployment {
    private String name;
    private String description;
    private String deploymentType;
    private String siteIdentity;
    private String active;
    private String isDefault;
    private String createdBy;
    private String apVersion;
    private List<String> applications = null;

    public String getName() {
        return name;
    }

    public AddDeployment setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public AddDeployment setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getDeploymentType() {
        return deploymentType;
    }

    public AddDeployment setDeploymentType(String deploymentType) {
        this.deploymentType = deploymentType;
        return this;
    }

    public String getSiteIdentity() {
        return siteIdentity;
    }

    public AddDeployment setSiteIdentity(String siteIdentity) {
        this.siteIdentity = siteIdentity;
        return this;
    }

    public String getActive() {
        return active;
    }

    public AddDeployment setActive(String active) {
        this.active = active;
        return this;
    }

    public String getIsDefault() {
        return isDefault;
    }

    public AddDeployment setIsDefault(String isDefault) {
        this.isDefault = isDefault;
        return this;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public AddDeployment setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public String getApVersion() {
        return apVersion;
    }

    public AddDeployment setApVersion(String apVersion) {
        this.apVersion = apVersion;
        return this;
    }

    public List<String> getApplications() {
        return applications;
    }

    public AddDeployment setApplications(List<String> applications) {
        this.applications = applications;
        return this;
    }
}