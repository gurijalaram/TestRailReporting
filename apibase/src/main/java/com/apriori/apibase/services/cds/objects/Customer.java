package com.apriori.apibase.services.cds.objects;

import com.apriori.utils.http.enums.Schema;
import com.apriori.utils.json.deserializers.DateTimeDeserializer_yyyyMMddTHHmmZ;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.time.LocalDateTime;
import java.util.List;


@Schema(location = "CdsCustomerSchema.json")
public class Customer {
    private String identity;
    private String createdBy;
    private String name;
    private String cloudReference;
    private String description;
    private List<String> emailRegexPatterns;
    private Boolean active;
    private String customerType;
    private Customer response;
    private String salesforceId;
    private String updatedBy;

    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmZ.class)
    private LocalDateTime createdAt;
    
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmZ.class)
    private LocalDateTime updatedAt;

    public String getCloudReference() {
        return this.cloudReference;
    }

    public void setCloudReference(String cloudReference) {
        this.cloudReference = cloudReference;
    }

    public String getUpdatedBy() {
        return this.updatedBy;
    }

    public Customer setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
        return this;
    }

    public String getUpdateBy() {
        return this.updatedBy;
    }

    public Customer setUpdateBy(String updateBy) {
        this.updatedBy = updateBy;
        return this;
    }

    public LocalDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public Customer setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public String getSalesforceId() {
        return this.salesforceId;
    }

    public Customer setSalesforceId(String salesforceId) {
        this.salesforceId = salesforceId;
        return this;
    }

    public Customer getResponse() {
        return this.response;
    }

    public Customer setResponse(Customer response) {
        this.response = response;
        return this;
    }

    public String getIdentity() {
        return this.identity;
    }

    public Customer setIdentity(String identity) {
        this.identity = identity;
        return this;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public Customer setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public Customer setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Customer setName(String name) {
        this.name = name;
        return this;
    }

    public Customer setCloudReferenece(String cloudReference) {
        this.cloudReference = cloudReference;
        return this;
    }

    public String getCloudReferenece() {
        return this.cloudReference;
    }

    public String getDescription() {
        return this.description;
    }

    public Customer setDescription(String description) {
        this.description = description;
        return this;
    }

    public Customer setEmailRegexPatterns(List<String> emailRegexPatterns) {
        this.emailRegexPatterns = emailRegexPatterns;
        return this;
    }

    public List<String> getEmailRegexPatterns() {
        return this.emailRegexPatterns;
    }

    public Customer setActive(Boolean active) {
        this.active = active;
        return this;
    }

    public Boolean getActive() {
        return this.active;
    }

    public Customer setCustomerType(String customerType) {
        this.customerType = customerType;
        return this;
    }

    public String getCustomerType() {
        return this.customerType;
    }

}
