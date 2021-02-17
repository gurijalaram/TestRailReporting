package com.apriori.cds.objects.response;

import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "cds/AccessControlSchema.json")
public class AccessControl {
    @JsonProperty
    private AccessControl response;
    @JsonProperty
    private String identity;
    @JsonProperty
    private String createdBy;
    @JsonProperty
    private String createdAt;
    @JsonProperty
    private String customerIdentity;
    @JsonProperty
    private String deploymentIdentity;
    @JsonProperty
    private String userIdentity;
    @JsonProperty
    private String installationIdentity;
    @JsonProperty
    private String applicationIdentity;
    @JsonProperty
    private String roleIdentity;
    @JsonProperty
    private Boolean outOfContext;

    public AccessControl getResponse() {
        return response;
    }

    public AccessControl setResponse(AccessControl response) {
        this.response = response;
        return this;
    }

    public String getIdentity() {
        return identity;
    }

    public AccessControl setIdentity(String identity) {
        this.identity = identity;
        return this;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public AccessControl setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public AccessControl setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public String getCustomerIdentity() {
        return customerIdentity;
    }

    public AccessControl setCustomerIdentity(String customerIdentity) {
        this.customerIdentity = customerIdentity;
        return this;
    }

    public String getDeploymentIdentity() {
        return deploymentIdentity;
    }

    public AccessControl setDeploymentIdentity(String deploymentIdentity) {
        this.deploymentIdentity = deploymentIdentity;
        return this;
    }

    public String getUserIdentity() {
        return userIdentity;
    }

    public AccessControl setUserIdentity(String userIdentity) {
        this.userIdentity = userIdentity;
        return this;
    }

    public String getInstallationIdentity() {
        return installationIdentity;
    }

    public AccessControl setInstallationIdentity(String installationIdentity) {
        this.installationIdentity = installationIdentity;
        return this;
    }

    public String getApplicationIdentity() {
        return applicationIdentity;
    }

    public AccessControl setApplicationIdentity(String applicationIdentity) {
        this.applicationIdentity = applicationIdentity;
        return this;
    }

    public String getRoleIdentity() {
        return roleIdentity;
    }

    public AccessControl setRoleIdentity(String roleIdentity) {
        this.roleIdentity = roleIdentity;
        return this;
    }

    public Boolean getOutOfContext() {
        return outOfContext;
    }

    public AccessControl setOutOfContext(Boolean outOfContext) {
        this.outOfContext = outOfContext;
        return this;
    }
}
