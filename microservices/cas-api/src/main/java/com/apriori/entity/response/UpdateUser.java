package com.apriori.entity.response;

import com.apriori.utils.http.enums.Schema;
import com.apriori.utils.json.deserializers.DateTimeDeserializer_yyyyMMddTHHmmZ;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "UpdateUserSchema.json")
public class UpdateUser {
    private UpdateUser response;
    private String identity;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmZ.class)
    private LocalDateTime createdAt;
    private String createdBy;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmZ.class)
    private LocalDateTime updatedAt;
    private String updatedBy;
    private String customerIdentity;
    @JsonProperty
    private UpdatedProfile userProfile;
    private String email;
    private String username;
    private Boolean active;
    private Boolean mfaRequired;
    private CustomProperties customProperties;
    private String createdByName;
    private String updatedByName;
    private List<Object> licenseAssignments = null;
    private String userType;

    public UpdateUser() {
    }

    public String getIdentity() {
        return identity;
    }

    public UpdateUser setIdentity(String identity) {
        this.identity = identity;
        return this;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public UpdateUser setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public UpdateUser setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public UpdateUser setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public UpdateUser setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
        return this;
    }

    public String getCustomerIdentity() {
        return customerIdentity;
    }

    public UpdateUser setCustomerIdentity(String customerIdentity) {
        this.customerIdentity = customerIdentity;
        return this;
    }

    public UpdatedProfile getUserProfile() {
        return userProfile;
    }

    public UpdateUser setUserProfile(UpdatedProfile userProfile) {
        this.userProfile = userProfile;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UpdateUser setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public UpdateUser setUsername(String username) {
        this.username = username;
        return this;
    }

    public Boolean getActive() {
        return active;
    }

    public UpdateUser setActive(Boolean active) {
        this.active = active;
        return this;
    }

    public Boolean getMfaRequired() {
        return mfaRequired;
    }

    public UpdateUser setMfaRequired(Boolean mfaRequired) {
        this.mfaRequired = mfaRequired;
        return this;
    }

    public CustomProperties getCustomProperties() {
        return customProperties;
    }

    public UpdateUser setCustomProperties(CustomProperties customProperties) {
        this.customProperties = customProperties;
        return this;
    }

    public String getCreatedByName() {
        return createdByName;
    }

    public UpdateUser setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
        return this;
    }

    public String getUpdatedByName() {
        return updatedByName;
    }

    public UpdateUser setUpdatedByName(String updatedByName) {
        this.updatedByName = updatedByName;
        return this;
    }

    public List<Object> getLicenseAssignments() {
        return licenseAssignments;
    }

    public UpdateUser setLicenseAssignments(List<Object> licenseAssignments) {
        this.licenseAssignments = licenseAssignments;
        return this;
    }

    public String getUserType() {
        return userType;
    }

    public UpdateUser setUserType(String userType) {
        this.userType = userType;
        return this;
    }

    public UpdateUser getResponse() {
        return response;
    }

    public UpdateUser setResponse(UpdateUser response) {
        this.response = response;
        return this;
    }
}
