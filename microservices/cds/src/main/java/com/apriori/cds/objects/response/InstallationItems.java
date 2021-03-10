package com.apriori.cds.objects.response;

import com.apriori.utils.http.enums.Schema;
import com.apriori.utils.json.deserializers.DateTimeDeserializer_yyyyMMddTHHmmZ;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "cds/InstallationSchema.json")
public class InstallationItems {
    private Boolean active;
    private String cidApiId;
    private String cidApiSecret;
    private String cidGlobalKey;
    private String clientId;
    private String clientSecret;
    private String cloudReference;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmZ.class)
    private LocalDateTime createdAt;
    private String createdBy;
    private String customerIdentity;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmZ.class)
    private LocalDateTime deletedAt;
    private String deletedBy;
    private String deploymentIdentity;
    private String description;
    private String identity;
    private String name;
    private String realm;
    private String region;
    private String s3Bucket;
    private String tenant;
    private String tenantGroup;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmZ.class)
    private LocalDateTime updatedAt;
    private String updatedBy;
    private String url;
    private List<Object> applications = null;
    private InstallationItems response;

    public List<Object> getApplications() {
        return applications;
    }

    public InstallationItems setApplications(List<Object> applications) {
        this.applications = applications;
        return this;
    }

    public InstallationItems getResponse() {
        return response;
    }

    public InstallationItems setResponse(InstallationItems response) {
        this.response = response;
        return this;
    }

    public Boolean getActive() {
        return active;
    }

    public InstallationItems setActive(Boolean active) {
        this.active = active;
        return this;
    }

    public String getCidApiId() {
        return cidApiId;
    }

    public InstallationItems setCidApiId(String cidApiId) {
        this.cidApiId = cidApiId;
        return this;
    }

    public String getCidApiSecret() {
        return cidApiSecret;
    }

    public InstallationItems setCidApiSecret(String cidApiSecret) {
        this.cidApiSecret = cidApiSecret;
        return this;
    }

    public String getCidGlobalKey() {
        return cidGlobalKey;
    }

    public InstallationItems setCidGlobalKey(String cidGlobalKey) {
        this.cidGlobalKey = cidGlobalKey;
        return this;
    }

    public String getClientId() {
        return clientId;
    }

    public InstallationItems setClientId(String clientId) {
        this.clientId = clientId;
        return this;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public InstallationItems setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
        return this;
    }

    public String getCloudReference() {
        return cloudReference;
    }

    public InstallationItems setCloudReference(String cloudReference) {
        this.cloudReference = cloudReference;
        return this;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public InstallationItems setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public InstallationItems setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public String getCustomerIdentity() {
        return customerIdentity;
    }

    public InstallationItems setCustomerIdentity(String customerIdentity) {
        this.customerIdentity = customerIdentity;
        return this;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public InstallationItems setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
        return this;
    }

    public String getDeletedBy() {
        return deletedBy;
    }

    public InstallationItems setDeletedBy(String deletedBy) {
        this.deletedBy = deletedBy;
        return this;
    }

    public String getDeploymentIdentity() {
        return deploymentIdentity;
    }

    public InstallationItems setDeploymentIdentity(String deploymentIdentity) {
        this.deploymentIdentity = deploymentIdentity;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public InstallationItems setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getIdentity() {
        return identity;
    }

    public InstallationItems setIdentity(String identity) {
        this.identity = identity;
        return this;
    }

    public String getName() {
        return name;
    }

    public InstallationItems setName(String name) {
        this.name = name;
        return this;
    }

    public String getRealm() {
        return realm;
    }

    public InstallationItems setRealm(String realm) {
        this.realm = realm;
        return this;
    }

    public String getRegion() {
        return region;
    }

    public InstallationItems setRegion(String region) {
        this.region = region;
        return this;
    }

    public String getS3Bucket() {
        return s3Bucket;
    }

    public InstallationItems setS3Bucket(String s3Bucket) {
        this.s3Bucket = s3Bucket;
        return this;
    }

    public String getTenant() {
        return tenant;
    }

    public InstallationItems setTenant(String tenant) {
        this.tenant = tenant;
        return this;
    }

    public String getTenantGroup() {
        return tenantGroup;
    }

    public InstallationItems setTenantGroup(String tenantGroup) {
        this.tenantGroup = tenantGroup;
        return this;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public InstallationItems setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public InstallationItems setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public InstallationItems setUrl(String url) {
        this.url = url;
        return this;
    }
}