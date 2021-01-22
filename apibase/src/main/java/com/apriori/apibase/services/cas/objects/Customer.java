package com.apriori.apibase.services.cas.objects;

import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Schema(location = "cas/CasCustomerSchema.json")
public class Customer {

    @JsonProperty("identity")
    private String identity;
    @JsonProperty("createdAt")
    private String createdAt;
    @JsonProperty("createdBy")
    private String createdBy;
    @JsonProperty("updatedAt")
    private String updatedAt;
    @JsonProperty("updatedBy")
    private String updatedBy;
    @JsonProperty("name")
    private String name;
    @JsonProperty("cloudReference")
    private String cloudReference;
    @JsonProperty("description")
    private String description;
    @JsonProperty("customerType")
    private String customerType;
    @JsonProperty("salesforceId")
    private String salesforceId;
    @JsonProperty("active")
    private Boolean active;
    @JsonProperty("maxCadFileRetentionDays")
    private Integer maxCadFileRetentionDays;
    @JsonProperty("useExternalIdentityProvider")
    private Boolean useExternalIdentityProvider;
    @JsonProperty("mfaRequired")
    private Boolean mfaRequired;
    @JsonProperty("oneTimePasswordApplications")
    private List<Object> oneTimePasswordApplications = null;
    @JsonProperty("createdByName")
    private String createdByName;
    @JsonProperty("updatedByName")
    private String updatedByName;
    @JsonProperty("identityProviders")
    private List<Object> identityProviders = null;
    @JsonProperty("emailDomains")
    private List<String> emailDomains = null;
    @JsonProperty("authenticationType")
    private String authenticationType;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("identity")
    public String getIdentity() {
        return identity;
    }

    @JsonProperty("identity")
    public void setIdentity(String identity) {
        this.identity = identity;
    }

    @JsonProperty("createdAt")
    public String getCreatedAt() {
        return createdAt;
    }

    @JsonProperty("createdAt")
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @JsonProperty("createdBy")
    public String getCreatedBy() {
        return createdBy;
    }

    @JsonProperty("createdBy")
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @JsonProperty("updatedAt")
    public String getUpdatedAt() {
        return updatedAt;
    }

    @JsonProperty("updatedAt")
    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    @JsonProperty("updatedBy")
    public String getUpdatedBy() {
        return updatedBy;
    }

    @JsonProperty("updatedBy")
    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("cloudReference")
    public String getCloudReference() {
        return cloudReference;
    }

    @JsonProperty("cloudReference")
    public void setCloudReference(String cloudReference) {
        this.cloudReference = cloudReference;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("customerType")
    public String getCustomerType() {
        return customerType;
    }

    @JsonProperty("customerType")
    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    @JsonProperty("salesforceId")
    public String getSalesforceId() {
        return salesforceId;
    }

    @JsonProperty("salesforceId")
    public void setSalesforceId(String salesforceId) {
        this.salesforceId = salesforceId;
    }

    @JsonProperty("active")
    public Boolean getActive() {
        return active;
    }

    @JsonProperty("active")
    public void setActive(Boolean active) {
        this.active = active;
    }

    @JsonProperty("maxCadFileRetentionDays")
    public Integer getMaxCadFileRetentionDays() {
        return maxCadFileRetentionDays;
    }

    @JsonProperty("maxCadFileRetentionDays")
    public void setMaxCadFileRetentionDays(Integer maxCadFileRetentionDays) {
        this.maxCadFileRetentionDays = maxCadFileRetentionDays;
    }

    @JsonProperty("useExternalIdentityProvider")
    public Boolean getUseExternalIdentityProvider() {
        return useExternalIdentityProvider;
    }

    @JsonProperty("useExternalIdentityProvider")
    public void setUseExternalIdentityProvider(Boolean useExternalIdentityProvider) {
        this.useExternalIdentityProvider = useExternalIdentityProvider;
    }

    @JsonProperty("mfaRequired")
    public Boolean getMfaRequired() {
        return mfaRequired;
    }

    @JsonProperty("mfaRequired")
    public void setMfaRequired(Boolean mfaRequired) {
        this.mfaRequired = mfaRequired;
    }

    @JsonProperty("oneTimePasswordApplications")
    public List<Object> getOneTimePasswordApplications() {
        return oneTimePasswordApplications;
    }

    @JsonProperty("oneTimePasswordApplications")
    public void setOneTimePasswordApplications(List<Object> oneTimePasswordApplications) {
        this.oneTimePasswordApplications = oneTimePasswordApplications;
    }

    @JsonProperty("createdByName")
    public String getCreatedByName() {
        return createdByName;
    }

    @JsonProperty("createdByName")
    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }

    @JsonProperty("updatedByName")
    public String getUpdatedByName() {
        return updatedByName;
    }

    @JsonProperty("updatedByName")
    public void setUpdatedByName(String updatedByName) {
        this.updatedByName = updatedByName;
    }

    @JsonProperty("identityProviders")
    public List<Object> getIdentityProviders() {
        return identityProviders;
    }

    @JsonProperty("identityProviders")
    public void setIdentityProviders(List<Object> identityProviders) {
        this.identityProviders = identityProviders;
    }

    @JsonProperty("emailDomains")
    public List<String> getEmailDomains() {
        return emailDomains;
    }

    @JsonProperty("emailDomains")
    public void setEmailDomains(List<String> emailDomains) {
        this.emailDomains = emailDomains;
    }

    @JsonProperty("authenticationType")
    public String getAuthenticationType() {
        return authenticationType;
    }

    @JsonProperty("authenticationType")
    public void setAuthenticationType(String authenticationType) {
        this.authenticationType = authenticationType;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
