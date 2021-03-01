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
@Schema(location = "cas/UserSchema.json")
public class User {
    private User response;
    private String identity;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmZ.class)
    private LocalDateTime createdAt;
    private String createdBy;
    private String customerIdentity;
    private UserProfile userProfile;
    private String email;
    private String username;
    private Boolean active;
    private Boolean mfaRequired;
    private CustomProperties customProperties;
    private String createdByName;
    private List<Object> licenseAssignments = null;
    private String userType;

    public User getResponse() {
        return response;
    }

    public User setResponse(User response) {
        this.response = response;
        return this;
    }

    public String getIdentity() {
        return identity;
    }

    public User setIdentity(String identity) {
        this.identity = identity;
        return this;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public User setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public User setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public String getCustomerIdentity() {
        return customerIdentity;
    }

    public User setCustomerIdentity(String customerIdentity) {
        this.customerIdentity = customerIdentity;
        return this;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public User setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public User setUsername(String username) {
        this.username = username;
        return this;
    }

    public Boolean getActive() {
        return active;
    }

    public User setActive(Boolean active) {
        this.active = active;
        return this;
    }

    public Boolean getMfaRequired() {
        return mfaRequired;
    }

    public User setMfaRequired(Boolean mfaRequired) {
        this.mfaRequired = mfaRequired;
        return this;
    }

    public CustomProperties getCustomProperties() {
        return customProperties;
    }

    public User setCustomProperties(CustomProperties customProperties) {
        this.customProperties = customProperties;
        return this;
    }

    public String getCreatedByName() {
        return createdByName;
    }

    public User setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
        return this;
    }

    public List<Object> getLicenseAssignments() {
        return licenseAssignments;
    }

    public User setLicenseAssignments(List<Object> licenseAssignments) {
        this.licenseAssignments = licenseAssignments;
        return this;
    }

    public String getUserType() {
        return userType;
    }

    public User setUserType(String userType) {
        this.userType = userType;
        return this;
    }
}
