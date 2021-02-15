package com.apriori.cds.objects.response;

import com.apriori.utils.http.enums.Schema;
import com.apriori.utils.json.deserializers.DateTimeDeserializer_yyyyMMddTHHmmZ;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "cds/ApplicationSchema.json")
public class Application {
    @JsonProperty
    private Boolean isCloudHomeApp;
    @JsonProperty
    private Boolean isSingleTenant;
    @JsonProperty
    private Boolean isPublic;
    @JsonProperty
    private String identity;
    @JsonProperty
    private String createdBy;
    @JsonProperty
    private String name;
    @JsonProperty
    private String cloudReference;
    @JsonProperty
    private String serviceName;
    @JsonProperty
    private String version;
    @JsonProperty
    private String description;
    @JsonProperty
    private String iconUrl;
    @JsonProperty
    private Application response;

    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmZ.class)
    private LocalDateTime createdAt;

    public Application getResponse() {
        return this.response;
    }

    public Application setResponse(Application response) {
        this.response = response;
        return this;
    }

    public Boolean getIsCloudHomeApp() {
        return this.isCloudHomeApp;
    }

    public Application setIsCloudhomeApp(Boolean isCloudHomeApp) {
        this.isCloudHomeApp = isCloudHomeApp;
        return this;
    }

    public Boolean getIsSingleTenant() {
        return this.isSingleTenant;
    }

    public Application setIsSingleTenant(Boolean isSingleTenant) {
        this.isSingleTenant = isSingleTenant;
        return this;
    }

    public Boolean getIsPublic() {
        return this.isPublic;
    }

    public Application setIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
        return this;
    }

    public String getIdentity() {
        return this.identity;
    }

    public Application setIdentity(String identity) {
        this.identity = identity;
        return this;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public Application setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public Application setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Application setName(String name) {
        this.name = name;
        return this;
    }

    public String getCloudReference() {
        return this.cloudReference;
    }

    public Application setCloudReference(String cloudReference) {
        this.cloudReference = cloudReference;
        return this;
    }

    public String getServiceName() {
        return this.serviceName;
    }

    public Application setServiceName(String serviceName) {
        this.serviceName = serviceName;
        return this;
    }

    public String getVersion() {
        return this.version;
    }

    public Application setVersion(String version) {
        this.version = version;
        return this;
    }

    public String getDescription() {
        return this.description;
    }

    public Application setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getIconUrl() {
        return this.iconUrl;
    }

    public Application setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
        return this;
    }


}
