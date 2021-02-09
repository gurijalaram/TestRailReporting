package com.apriori.cds.entity.response;

import com.apriori.apibase.utils.TestUtil;
import com.apriori.utils.http.enums.Schema;
import com.apriori.utils.json.deserializers.DateTimeDeserializer_yyyyMMddTHHmmZ;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.time.LocalDateTime;
import java.util.List;


@Schema(location = "cds/CdsCustomerSchema.json")
public class Customer extends TestUtil {

    private Customer response;
    private Boolean useExternalIdentityProvider;
    private Boolean mfaRequired;
    private List<String> oneTimePasswordApplications;
    private Integer maxCadFileRetentionDays;
    private String identity;
    private String createdBy;
    private String name;
    private String cloudReference;
    private String description;
    private List<String> emailRegexPatterns;
    private Boolean active;
    private String customerType;
    private String salesforceId;
    private String updatedBy;

    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmZ.class)
    private LocalDateTime createdAt;

    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmZ.class)
    private LocalDateTime updatedAt;

    public Boolean getMfaRequired() {
        return mfaRequired;
    }

    public Customer setMfaRequired(Boolean mfaRequired) {
        this.mfaRequired = mfaRequired;
        return this;
    }

    public List<String> getOneTimePasswordApplications() {
        return oneTimePasswordApplications;
    }

    public Customer setOneTimePasswordApplications(List<String> oneTimePasswordApplications) {
        this.oneTimePasswordApplications = oneTimePasswordApplications;
        return this;
    }

    public Boolean getUseExternalIdentityProvider() {
        return useExternalIdentityProvider;
    }

    public Customer setUseExternalIdentityProvider(Boolean useExternalIdentityProvider) {
        this.useExternalIdentityProvider = useExternalIdentityProvider;
        return this;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Customer setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public LocalDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public Customer setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public Customer getResponse() {
        return this.response;
    }

    public Customer setResponse(Customer response) {
        this.response = response;
        return this;
    }

    public Integer getMaxCadFileRetentionDays() {
        return this.maxCadFileRetentionDays;
    }

    public Customer setMaxCadFileRetentionDays(Integer maxCadFileRetentionDays) {
        this.maxCadFileRetentionDays = maxCadFileRetentionDays;
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

    public String getName() {
        return this.name;
    }

    public Customer setName(String name) {
        this.name = name;
        return this;
    }

    public String getCloudReference() {
        return this.cloudReference;
    }

    public Customer setCloudReference(String cloudReference) {
        this.cloudReference = cloudReference;
        return this;
    }

    public String getDescription() {
        return this.description;
    }

    public Customer setDescription(String description) {
        this.description = description;
        return this;
    }

    public List<String> getEmailRegexPatterns() {
        return this.emailRegexPatterns;
    }

    public Customer setEmailRegexPatterns(List<String> emailRegexPatterns) {
        this.emailRegexPatterns = emailRegexPatterns;
        return this;
    }

    public Boolean getActive() {
        return this.active;
    }

    public Customer setActive(Boolean active) {
        this.active = active;
        return this;
    }

    public String getCustomerType() {
        return this.customerType;
    }

    public Customer setCustomerType(String customerType) {
        this.customerType = customerType;
        return this;
    }

    public String getSalesforceId() {
        return this.salesforceId;
    }

    public Customer setSalesforceId(String salesforceId) {
        this.salesforceId = salesforceId;
        return this;
    }

    public String getUpdatedBy() {
        return this.updatedBy;
    }

    public Customer setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
        return this;
    }
}
