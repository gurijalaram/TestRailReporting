package com.apriori.cds.objects.response;

import com.apriori.utils.http.enums.Schema;
import com.apriori.utils.json.deserializers.DateTimeDeserializer_yyyyMMddTHHmmZ;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "cds/AccessControlSchema.json")
public class AccessControlResponse {
    private AccessControlResponse response;
    private String identity;
    private String createdBy;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmZ.class)
    private LocalDateTime createdAt;
    private String customerIdentity;
    private String deploymentIdentity;
    private String userIdentity;
    private String installationIdentity;
    private String applicationIdentity;
    private String roleIdentity;
    private Boolean outOfContext;
    private String customerAssociationIdentity;

    public AccessControlResponse getResponse() {
        return response;
    }

    public AccessControlResponse setResponse(AccessControlResponse response) {
        this.response = response;
        return this;
    }

    public String getIdentity() {
        return identity;
    }

    public AccessControlResponse setIdentity(String identity) {
        this.identity = identity;
        return this;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public AccessControlResponse setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public AccessControlResponse setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public String getCustomerIdentity() {
        return customerIdentity;
    }

    public AccessControlResponse setCustomerIdentity(String customerIdentity) {
        this.customerIdentity = customerIdentity;
        return this;
    }

    public String getDeploymentIdentity() {
        return deploymentIdentity;
    }

    public AccessControlResponse setDeploymentIdentity(String deploymentIdentity) {
        this.deploymentIdentity = deploymentIdentity;
        return this;
    }

    public String getUserIdentity() {
        return userIdentity;
    }

    public AccessControlResponse setUserIdentity(String userIdentity) {
        this.userIdentity = userIdentity;
        return this;
    }

    public String getInstallationIdentity() {
        return installationIdentity;
    }

    public AccessControlResponse setInstallationIdentity(String installationIdentity) {
        this.installationIdentity = installationIdentity;
        return this;
    }

    public String getApplicationIdentity() {
        return applicationIdentity;
    }

    public AccessControlResponse setApplicationIdentity(String applicationIdentity) {
        this.applicationIdentity = applicationIdentity;
        return this;
    }

    public String getRoleIdentity() {
        return roleIdentity;
    }

    public AccessControlResponse setRoleIdentity(String roleIdentity) {
        this.roleIdentity = roleIdentity;
        return this;
    }

    public Boolean getOutOfContext() {
        return outOfContext;
    }

    public AccessControlResponse setOutOfContext(Boolean outOfContext) {
        this.outOfContext = outOfContext;
        return this;
    }

    public String getCustomerAssociationIdentity() {
        return customerAssociationIdentity;
    }

    public AccessControlResponse setCustomerAssociationIdentity(String customerAssociationIdentity) {
        this.customerAssociationIdentity = customerAssociationIdentity;
        return this;
    }
}
