package com.apriori.apibase.services.cds.objects.AccessControls;

public class AccessControlItems {

    private String identity;
    private String createdBy;
    private String createdAt;
    private String customerIdentity;
    private String deploymentIdentity;
    private String userIdentity;
    private String installationIdentity;
    private String applicationIdentity;
    private String roleIdentity;
    private Boolean outOfContext;

    public String getIdentity() {
        return identity;
    }

    public AccessControlItems setIdentity(String identity) {
        this.identity = identity;
        return this;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public AccessControlItems setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public AccessControlItems  setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public String getCustomerIdentity() {
        return customerIdentity;
    }

    public AccessControlItems  setCustomerIdentity(String customerIdentity) {
        this.customerIdentity = customerIdentity;
        return this;
    }

    public String getDeploymentIdentity() {
        return deploymentIdentity;
    }

    public AccessControlItems  setDeploymentIdentity(String deploymentIdentity) {
        this.deploymentIdentity = deploymentIdentity;
        return this;
    }

    public String getUserIdentity() {
        return userIdentity;
    }

    public AccessControlItems  setUserIdentity(String userIdentity) {
        this.userIdentity = userIdentity;
        return this;
    }

    public String getInstallationIdentity() {
        return installationIdentity;
    }

    public AccessControlItems  setInstallationIdentity(String installationIdentity) {
        this.installationIdentity = installationIdentity;
        return this;
    }

    public String getApplicationIdentity() {
        return applicationIdentity;
    }

    public AccessControlItems  setApplicationIdentity(String applicationIdentity) {
        this.applicationIdentity = applicationIdentity;
        return this;
    }

    public String getRoleIdentity() {
        return roleIdentity;
    }

    public AccessControlItems  setRoleIdentity(String roleIdentity) {
        this.roleIdentity = roleIdentity;
        return this;
    }

    public Boolean getOutOfContext() {
        return outOfContext;
    }

    public AccessControlItems  setOutOfContext(Boolean outOfContext) {
        this.outOfContext = outOfContext;
        return this;
    }
}
