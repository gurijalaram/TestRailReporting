package com.apriori.entity.response;

import com.apriori.utils.http.enums.Schema;
import com.apriori.utils.json.deserializers.DateTimeDeserializer_yyyyMMddTHHmmZ;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "cas/CustomerUserSchema.json")
public class CustomerUser {
    private CustomerUser response;
    private String identity;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmZ.class)
    private LocalDateTime createdAt;
    private String createdBy;
    private String customerIdentity;
    private CustomerUserProfile userProfile;
    private String email;
    private String username;
    private Boolean active;
    private Boolean mfaRequired;
    private CustomProperties customProperties;
    private String createdByName;
    private List<Object> licenseAssignments = null;
    private String userType;

    public CustomerUser getResponse() {
        return response;
    }

    public CustomerUser setResponse(CustomerUser response) {
        this.response = response;
        return this;
    }

    public String getIdentity() {
        return identity;
    }

    public CustomerUser setIdentity(String identity) {
        this.identity = identity;
        return this;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public CustomerUser setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public CustomerUser setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public String getCustomerIdentity() {
        return customerIdentity;
    }

    public CustomerUser setCustomerIdentity(String customerIdentity) {
        this.customerIdentity = customerIdentity;
        return this;
    }

    public CustomerUserProfile getUserProfile() {
        return userProfile;
    }

    public CustomerUser setUserProfile(CustomerUserProfile userProfile) {
        this.userProfile = userProfile;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public CustomerUser setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public CustomerUser setUsername(String username) {
        this.username = username;
        return this;
    }

    public Boolean getActive() {
        return active;
    }

    public CustomerUser setActive(Boolean active) {
        this.active = active;
        return this;
    }

    public Boolean getMfaRequired() {
        return mfaRequired;
    }

    public CustomerUser setMfaRequired(Boolean mfaRequired) {
        this.mfaRequired = mfaRequired;
        return this;
    }

    public CustomProperties getCustomProperties() {
        return customProperties;
    }

    public CustomerUser setCustomProperties(CustomProperties customProperties) {
        this.customProperties = customProperties;
        return this;
    }

    public String getCreatedByName() {
        return createdByName;
    }

    public CustomerUser setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
        return this;
    }

    public List<Object> getLicenseAssignments() {
        return licenseAssignments;
    }

    public CustomerUser setLicenseAssignments(List<Object> licenseAssignments) {
        this.licenseAssignments = licenseAssignments;
        return this;
    }

    public String getUserType() {
        return userType;
    }

    public CustomerUser setUserType(String userType) {
        this.userType = userType;
        return this;
    }
}
