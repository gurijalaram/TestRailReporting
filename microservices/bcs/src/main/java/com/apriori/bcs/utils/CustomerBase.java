package com.apriori.bcs.utils;

import com.apriori.apibase.services.bcs.objects.CisCustomer;
import com.apriori.apibase.utils.TestUtil;

import java.util.ArrayList;
import java.util.List;

public class CustomerBase extends TestUtil {
    private String identity;
    private String createdBy;
    private String name;
    private String cloudReference;
    private String description;
    private List<String> emailRegexPatterns;
    private Boolean active;
    private Integer maxCadFileRetentionDays;
    private String customerType;
    private CisCustomer response;
    private String salesforceId;
    private String updatedBy;
    private Boolean useExternalIdentityProvider;
    private Boolean mfaRequired;
    private ArrayList oneTimePasswordApplications;

    public Boolean getUseExternalIdentityProvider() {
        return useExternalIdentityProvider;
    }

    public CustomerBase setUseExternalIdentityProvider(Boolean useExternalIdentityProvider) {
        this.useExternalIdentityProvider = useExternalIdentityProvider;
        return this;
    }

    public Boolean getMfaRequired() {
        return mfaRequired;
    }

    public CustomerBase setMfaRequired(Boolean mfaRequired) {
        this.mfaRequired = mfaRequired;
        return this;
    }

    public ArrayList getOneTimePasswordApplications() {
        return oneTimePasswordApplications;
    }

    public CustomerBase setOneTimePasswordApplications(ArrayList oneTimePasswordApplications) {
        this.oneTimePasswordApplications = oneTimePasswordApplications;
        return this;
    }

    public String getIdentity() {
        return this.identity;
    }

    public CustomerBase setIdentity(String identity) {
        this.identity = identity;
        return this;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public CustomerBase setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public CustomerBase setName(String name) {
        this.name = name;
        return this;
    }

    public String getCloudReference() {
        return this.cloudReference;
    }

    public CustomerBase setCloudReference(String cloudReference) {
        this.cloudReference = cloudReference;
        return this;
    }

    public String getDescription() {
        return this.description;
    }

    public CustomerBase setDescription(String description) {
        this.description = description;
        return this;
    }

    public List<String> getEmailRegexPatterns() {
        return this.emailRegexPatterns;
    }

    public CustomerBase setEmailRegexPatterns(List<String> emailRegexPatterns) {
        this.emailRegexPatterns = emailRegexPatterns;
        return this;
    }

    public Boolean getActive() {
        return this.active;
    }

    public CustomerBase setActive(Boolean active) {
        this.active = active;
        return this;
    }

    public Integer getMaxCadFileRetentionDays() {
        return this.maxCadFileRetentionDays;
    }

    public CustomerBase setMaxCadFileRetentionDays(Integer maxCadFileRetentionDays) {
        this.maxCadFileRetentionDays = maxCadFileRetentionDays;
        return this;
    }

    public String getCustomerType() {
        return this.customerType;
    }

    public CustomerBase setCustomerType(String customerType) {
        this.customerType = customerType;
        return this;
    }

    public String getSalesforceId() {
        return this.salesforceId;
    }

    public CustomerBase setSalesforceId(String salesforceId) {
        this.salesforceId = salesforceId;
        return this;
    }

    public String getUpdatedBy() {
        return this.updatedBy;
    }

    public CustomerBase setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
        return this;
    }
}
