package com.apriori.entity.response;

import com.apriori.utils.http.enums.Schema;
import com.apriori.utils.json.deserializers.DateTimeDeserializer_yyyyMMddTHHmmZ;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "InstallationSchema.json")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Installation {
    private String identity;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmZ.class)
    private LocalDateTime createdAt;
    private String createdBy;
    private String customerIdentity;
    private String deploymentIdentity;
    private String name;
    private String cloudReference;
    private String description;
    private Boolean active;
    private String apVersion;
    private String url;
    private String realm;
    private String s3Bucket;
    private String tenantGroup;
    private String tenant;
    private String clientId;
    private String clientSecret;
    private String cidGlobalKey;
    private String region;

    public String getIdentity() {
        return identity;
    }

    public Installation setIdentity(String identity) {
        this.identity = identity;
        return this;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Installation setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public Installation setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public String getCustomerIdentity() {
        return customerIdentity;
    }

    public Installation setCustomerIdentity(String customerIdentity) {
        this.customerIdentity = customerIdentity;
        return this;
    }

    public String getDeploymentIdentity() {
        return deploymentIdentity;
    }

    public Installation setDeploymentIdentity(String deploymentIdentity) {
        this.deploymentIdentity = deploymentIdentity;
        return this;
    }

    public String getName() {
        return name;
    }

    public Installation setName(String name) {
        this.name = name;
        return this;
    }

    public String getCloudReference() {
        return cloudReference;
    }

    public Installation setCloudReference(String cloudReference) {
        this.cloudReference = cloudReference;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Installation setDescription(String description) {
        this.description = description;
        return this;
    }

    public Boolean getActive() {
        return active;
    }

    public Installation setActive(Boolean active) {
        this.active = active;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public Installation setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getRealm() {
        return realm;
    }

    public Installation setRealm(String realm) {
        this.realm = realm;
        return this;
    }

    public String getS3Bucket() {
        return s3Bucket;
    }

    public Installation setS3Bucket(String s3Bucket) {
        this.s3Bucket = s3Bucket;
        return this;
    }

    public String getTenantGroup() {
        return tenantGroup;
    }

    public Installation setTenantGroup(String tenantGroup) {
        this.tenantGroup = tenantGroup;
        return this;
    }

    public String getTenant() {
        return tenant;
    }

    public Installation setTenant(String tenant) {
        this.tenant = tenant;
        return this;
    }

    public String getClientId() {
        return clientId;
    }

    public Installation setClientId(String clientId) {
        this.clientId = clientId;
        return this;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public Installation setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
        return this;
    }

    public String getCidGlobalKey() {
        return cidGlobalKey;
    }


    public Installation setCidGlobalKey(String cidGlobalKey) {
        this.cidGlobalKey = cidGlobalKey;
        return this;
    }

    public String getRegion() {
        return region;
    }

    public Installation setRegion(String region) {
        this.region = region;
        return this;
    }
}
