package com.apriori.apibase.services;

import com.apriori.apibase.services.cis.objects.CisCustomer;
import com.apriori.apibase.utils.TestUtil;

import java.util.List;

public class CustomerBase extends TestUtil {
    private String identity;
    private String createdBy;
    private String name;
    private String cloudReference;
    private String description;
    private List<String> emailRegexPatterns;
    private Boolean active;
    private String customerType;
    private CisCustomer response;
    private String salesforceId;
    private String updatedBy;

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
