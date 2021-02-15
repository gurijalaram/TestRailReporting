package com.apriori.apibase.services.cas;

import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "cas/SingleCustomerSchema.json")
public class SingleCustomer {
    @JsonProperty
    private SingleCustomer response;
    @JsonProperty
    private String identity;
    @JsonProperty
    private String createdAt;
    @JsonProperty
    private String createdBy;
    @JsonProperty
    private String name;
    @JsonProperty
    private String cloudReference;
    @JsonProperty
    private String description;
    @JsonProperty
    private String customerType;
    @JsonProperty
    private Boolean active;
    @JsonProperty
    private Integer maxCadFileRetentionDays;
    @JsonProperty
    private Boolean useExternalIdentityProvider;
    @JsonProperty
    private Boolean mfaRequired;
    @JsonProperty
    private List<Object> oneTimePasswordApplications = null;
    @JsonProperty
    private String createdByName;
    @JsonProperty
    private List<Object> identityProviders = null;
    @JsonProperty
    private List<String> emailDomains = null;
    @JsonProperty
    private String authenticationType;

    public SingleCustomer getResponse() {
        return response;
    }

    public SingleCustomer setResponse(SingleCustomer response) {
        this.response = response;
        return this;
    }

    public String getIdentity() {
        return identity;
    }

    public SingleCustomer setIdentity(String identity) {
        this.identity = identity;
        return this;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public SingleCustomer setCreatedAt(String createdAt) {
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

    public List<Object> getOneTimePasswordApplications() {
        return oneTimePasswordApplications;
    }

    public SingleCustomer setOneTimePasswordApplications(List<Object> oneTimePasswordApplications) {
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

    public List<Object> getIdentityProviders() {
        return identityProviders;
    }

    public SingleCustomer setIdentityProviders(List<Object> identityProviders) {
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
