package com.apriori.cds.objects.response;

import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "cds/DeploymentSchema.json")
public class Deployment {
    private Deployment response;
    private Boolean isDefault;
    private String identity;
    private String createdBy;
    private String createdAt;
    private String name;
    private String description;
    private String apVersion;
    private Boolean active;
    private List<Object> installations = null;
    private String customerIdentity;
    private String deploymentType;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Deployment getResponse() {
        return response;
    }

    public Deployment setResponse(Deployment response) {
        this.response = response;
        return this;
    }

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

    public String getDeploymentType() {
        return deploymentType;
    }

    public Deployment setDeploymentType(String deploymentType) {
        this.deploymentType = deploymentType;
        return this;
    }
}