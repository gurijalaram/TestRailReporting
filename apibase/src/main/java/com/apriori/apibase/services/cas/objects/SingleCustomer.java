package com.apriori.apibase.services.cas.objects;

import com.apriori.utils.json.deserializers.DateTimeDeserializer_yyyyMMddTHHmmZ;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.time.LocalDateTime;
import java.util.List;

public class SingleCustomer {
    @JsonProperty
    private String identity;
    @JsonProperty
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmZ.class)
    private LocalDateTime createdAt;
    @JsonProperty
    private String createdBy;
    @JsonProperty
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmZ.class)
    private LocalDateTime updatedAt;
    @JsonProperty
    private String updatedBy;
    @JsonProperty
    private String name;
    @JsonProperty
    private String cloudReference;
    @JsonProperty
    private String description;
    @JsonProperty
    private String customerType;
    @JsonProperty
    private String salesforceId;
    @JsonProperty
    private Boolean active;
    @JsonProperty
    private Integer maxCadFileRetentionDays;
    @JsonProperty
    private Boolean useExternalIdentityProvider;
    @JsonProperty
    private Boolean mfaRequired;
    @JsonProperty
    private List<SingleCustomer> oneTimePasswordApplications = null;
    @JsonProperty
    private String createdByName;
    @JsonProperty
    private String updatedByName;
    @JsonProperty
    private List<SingleCustomer> identityProviders = null;
    @JsonProperty
    private List<String> emailDomains = null;
    @JsonProperty
    private String authenticationType;

    public String getIdentity() {
        return identity;
    }

    public SingleCustomer setIdentity(String identity) {
        this.identity = identity;
        return this;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public SingleCustomer setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public SingleCustomer setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public SingleCustomer setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public SingleCustomer setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
        return this;
    }

    public String getName() {
        return name;
    }

    public SingleCustomer setName(String name) {
        this.name = name;
        return this;
    }

    public String getCloudReference() {
        return cloudReference;
    }

    public SingleCustomer setCloudReference(String cloudReference) {
        this.cloudReference = cloudReference;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public SingleCustomer setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getCustomerType() {
        return customerType;
    }

    public SingleCustomer setCustomerType(String customerType) {
        this.customerType = customerType;
        return this;
    }

    public String getSalesforceId() {
        return salesforceId;
    }

    public SingleCustomer setSalesforceId(String salesforceId) {
        this.salesforceId = salesforceId;
        return this;
    }

    public Boolean getActive() {
        return active;
    }

    public SingleCustomer setActive(Boolean active) {
        this.active = active;
        return this;
    }

    public Integer getMaxCadFileRetentionDays() {
        return maxCadFileRetentionDays;
    }

    public SingleCustomer setMaxCadFileRetentionDays(Integer maxCadFileRetentionDays) {
        this.maxCadFileRetentionDays = maxCadFileRetentionDays;
        return this;
    }

    public Boolean getUseExternalIdentityProvider() {
        return useExternalIdentityProvider;
    }

    public SingleCustomer setUseExternalIdentityProvider(Boolean useExternalIdentityProvider) {
        this.useExternalIdentityProvider = useExternalIdentityProvider;
        return this;
    }

    public Boolean getMfaRequired() {
        return mfaRequired;
    }

    public SingleCustomer setMfaRequired(Boolean mfaRequired) {
        this.mfaRequired = mfaRequired;
        return this;
    }

    public List<SingleCustomer> getOneTimePasswordApplications() {
        return oneTimePasswordApplications;
    }

    public SingleCustomer setOneTimePasswordApplications(List<SingleCustomer> oneTimePasswordApplications) {
        this.oneTimePasswordApplications = oneTimePasswordApplications;
        return this;
    }

    public String getCreatedByName() {
        return createdByName;
    }

    public SingleCustomer setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
        return this;
    }

    public String getUpdatedByName() {
        return updatedByName;
    }

    public SingleCustomer setUpdatedByName(String updatedByName) {
        this.updatedByName = updatedByName;
        return this;
    }

    public List<SingleCustomer> getIdentityProviders() {
        return identityProviders;
    }

    public SingleCustomer setIdentityProviders(List<SingleCustomer> identityProviders) {
        this.identityProviders = identityProviders;
        return this;
    }

    public List<String> getEmailDomains() {
        return emailDomains;
    }

    public SingleCustomer setEmailDomains(List<String> emailDomains) {
        this.emailDomains = emailDomains;
        return this;
    }

    public String getAuthenticationType() {
        return authenticationType;
    }

    public SingleCustomer setAuthenticationType(String authenticationType) {
        this.authenticationType = authenticationType;
        return this;
    }
}
