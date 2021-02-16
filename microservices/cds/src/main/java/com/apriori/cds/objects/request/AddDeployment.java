package com.apriori.cds.entity.request;

import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "cds/AddDeploymentSchema.json")
public class AddDeployment {
    private AddDeployment deployment;
    private String name;
    private String description;
    private String deploymentType;
    private String siteIdentity;
    private String active;
    private String isDefault;
    private String createdBy;
    private String apVersion;
    private List<String> applications = null;

    public AddDeployment getDeployment() {
        return deployment;
    }

    public AddDeployment setDeployment(AddDeployment deployment) {
        this.deployment = deployment;
        return this;
    }

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