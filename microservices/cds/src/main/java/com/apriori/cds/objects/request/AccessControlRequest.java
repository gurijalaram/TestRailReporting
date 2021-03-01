package com.apriori.cds.objects.request;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccessControlRequest {
    private String customerIdentity;
    private String deploymentIdentity;
    private String installationIdentity;
    private String applicationIdentity;
    private String createdBy;
    private String roleName;
    private String roleIdentity;

    public String getCustomerIdentity() {
        return customerIdentity;
    }

    public AccessControlRequest setCustomerIdentity(String customerIdentity) {
        this.customerIdentity = customerIdentity;
        return this;
    }

    public String getDeploymentIdentity() {
        return deploymentIdentity;
    }

    public AccessControlRequest setDeploymentIdentity(String deploymentIdentity) {
        this.deploymentIdentity = deploymentIdentity;
        return this;
    }

    public String getInstallationIdentity() {
        return installationIdentity;
    }

    public AccessControlRequest setInstallationIdentity(String installationIdentity) {
        this.installationIdentity = installationIdentity;
        return this;
    }

    public String getApplicationIdentity() {
        return applicationIdentity;
    }

    public AccessControlRequest setApplicationIdentity(String applicationIdentity) {
        this.applicationIdentity = applicationIdentity;
        return this;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public AccessControlRequest setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public String getRoleName() {
        return roleName;
    }

    public AccessControlRequest setRoleName(String roleName) {
        this.roleName = roleName;
        return this;
    }

    public String getRoleIdentity() {
        return roleIdentity;
    }

    public AccessControlRequest setRoleIdentity(String roleIdentity) {
        this.roleIdentity = roleIdentity;
        return this;
    }
}
