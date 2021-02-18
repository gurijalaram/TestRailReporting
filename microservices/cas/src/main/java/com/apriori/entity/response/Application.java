package com.apriori.entity.response;

import com.apriori.utils.http.enums.Schema;
import com.apriori.utils.json.deserializers.DateTimeDeserializer_yyyyMMddTHHmmZ;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "cas/ApplicationSchema.json")
public class Application {
    private Boolean isSingleTenant;
    private Boolean isCloudHomeApp;
    private Boolean isPublic;
    private String identity;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmZ.class)
    private LocalDateTime createdAt;
    private String createdBy;
    private String name;
    private String serviceName;
    private String cloudReference;
    private String description;
    private String iconUrl;

    public Boolean getIsSingleTenant() {
        return isSingleTenant;
    }

    public Application setIsSingleTenant(Boolean isSingleTenant) {
        this.isSingleTenant = isSingleTenant;
        return this;
    }

    public Boolean getIsCloudHomeApp() {
        return isCloudHomeApp;
    }

    public Application setIsCloudHomeApp(Boolean isCloudHomeApp) {
        this.isCloudHomeApp = isCloudHomeApp;
        return this;
    }

    public Boolean getIsPublic() {
        return isPublic;
    }

    public Application setIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
        return this;
    }

    public String getIdentity() {
        return identity;
    }

    public Application setIdentity(String identity) {
        this.identity = identity;
        return this;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Application setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public Application setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public String getName() {
        return name;
    }

    public Application setName(String name) {
        this.name = name;
        return this;
    }

    public String getServiceName() {
        return serviceName;
    }

    public Application setServiceName(String serviceName) {
        this.serviceName = serviceName;
        return this;
    }

    public String getCloudReference() {
        return cloudReference;
    }

    public Application setCloudReference(String cloudReference) {
        this.cloudReference = cloudReference;
        return this;
    }

    public String getDescription() {
        return description;

    }

    public Application setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public Application setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
        return this;
    }
}
