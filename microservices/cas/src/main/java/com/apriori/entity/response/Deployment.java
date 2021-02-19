package com.apriori.entity.response;

import com.apriori.utils.http.enums.Schema;
import com.apriori.utils.json.deserializers.DateTimeDeserializer_yyyyMMddTHHmmZ;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "cas/DeploymentSchema.json")
public class Deployment {
    private Deployment response;
    private Boolean isDefault;
    private String identity;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmZ.class)
    private LocalDateTime createdAt;
    private String createdBy;
    private String customerIdentity;
    private String name;
    private String description;
    private Boolean active;
    private String apVersion;
    private List<Application> applications = null;
    private List<Installation> installations = null;
    private List<Object> siteIdentities = null;
    private String deploymentType;

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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Deployment setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public Deployment setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public String getCustomerIdentity() {
        return customerIdentity;
    }

    public Deployment setCustomerIdentity(String customerIdentity) {
        this.customerIdentity = customerIdentity;
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

    public Boolean getActive() {
        return active;
    }

    public Deployment setActive(Boolean active) {
        this.active = active;
        return this;
    }

    public String getApVersion() {
        return apVersion;
    }

    public Deployment setApVersion(String apVersion) {
        this.apVersion = apVersion;
        return this;
    }

    public List<Application> getApplications() {
        return applications;
    }

    public Deployment setApplications(List<Application> applications) {
        this.applications = applications;
        return this;
    }

    public List<Installation> getInstallations() {
        return installations;
    }

    public Deployment setInstallations(List<Installation> installations) {
        this.installations = installations;
        return this;
    }

    public List<Object> getSiteIdentities() {
        return siteIdentities;
    }

    public Deployment setSiteIdentities(List<Object> siteIdentities) {
        this.siteIdentities = siteIdentities;
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
