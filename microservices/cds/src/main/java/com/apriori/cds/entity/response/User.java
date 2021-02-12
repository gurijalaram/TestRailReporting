package com.apriori.cds.entity.response;

import com.apriori.utils.http.enums.Schema;
import com.apriori.utils.json.deserializers.DateTimeDeserializer_yyyyMMddTHHmmZ;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.time.LocalDateTime;
import java.util.List;

@Schema(location = "cds/UserSchema.json")
public class User {
    @JsonProperty
    private String identity;
    @JsonProperty
    private String createdBy;
    @JsonProperty
    private String userType;
    @JsonProperty
    private UserProfile userProfile;
    @JsonProperty
    private String email;
    @JsonProperty
    private String username;
    @JsonProperty
    private Boolean active;
    @JsonProperty
    private List<UserSite> sites;
    @JsonProperty
    private CustomAttributes customAttributes;
    @JsonProperty
    private String customerIdentity;
    @JsonProperty
    private User response;
    @JsonProperty
    private String updatedBy;
    @JsonProperty
    private Boolean mfaRequired;

    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmZ.class)
    private LocalDateTime createdAt;

    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmZ.class)
    private LocalDateTime updatedAt;

    public Boolean getMfaRequired() {
        return mfaRequired;
    }

    public User setMfaRequired(Boolean mfaRequired) {
        this.mfaRequired = mfaRequired;
        return this;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public User setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public String getUpdatedBy() {
        return this.updatedBy;
    }

    public User setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
        return this;
    }

    public User getResponse() {
        return this.response;
    }

    public User setResponse(User response) {
        this.response = response;
        return this;
    }

    public String getIdentity() {
        return this.identity;
    }

    public User setIdentity(String identity) {
        this.identity = identity;
        return this;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public User setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public User setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public String getUserType() {
        return this.userType;
    }

    public User setUserType(String userType) {
        this.userType = userType;
        return this;
    }

    public UserProfile getUserProfile() {
        return this.userProfile;
    }

    public User setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
        return this;
    }

    public String getEmail() {
        return this.email;
    }

    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getUsername() {
        return this.username;
    }

    public User setUsername(String userName) {
        this.username = userName;
        return this;
    }

    public Boolean getActive() {
        return this.active;
    }

    public User setActive(Boolean active) {
        this.active = active;
        return this;
    }

    public List<UserSite> getSites() {
        return this.sites;
    }

    public User setSites(List<UserSite> sites) {
        this.sites = sites;
        return this;
    }

    public CustomAttributes getCustomAttributes() {
        return customAttributes;
    }

    public User setCustomAttributes(CustomAttributes customAttributes) {
        this.customAttributes = customAttributes;
        return this;
    }

    public String getCustomerIdentity() {
        return this.customerIdentity;
    }

    public User setCustomerIdentity(String customerIdentity) {
        this.customerIdentity = customerIdentity;
        return this;
    }
}
