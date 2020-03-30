package com.apriori.apibase.services.objects;

import com.apriori.apibase.http.enums.Schema;
import com.apriori.utils.json.deserializers.DateTimeDeserializer_yyyyMMddTHHmmZ;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.time.LocalDateTime;
import java.util.List;

@Schema(location = "CdsUserSchema.json")
public class User {
    private String identity;
    private String createdBy;
    private String userType;
    private UserProfile userProfile;
    private String email;
    private String username;
    private Boolean active;
    private List<Site> sites;
    private String customerIdentity;
    private User response;

    
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmZ.class)
    private LocalDateTime createdAt;

    public User getResponse() {
        return this.response;
    }

    public User setResponse(User response) {
        this.response = response;
        return this;
    }

    public User setIdentity(String identity) {
        this.identity = identity;
        return this;
    }

    public String getIdentity() {
        return this.identity;
    }

    public User setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public User setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public User setUserType(String userType) {
        this.userType = userType;
        return this;
    }

    public String getUserType() {
        return this.userType;
    }

    public User setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
        return this;
    }

    public UserProfile getUserProfile() {
        return this.userProfile;
    }

    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getEmail() {
        return this.email;
    }

    public User setUsername(String userName) {
        this.username = userName;
        return this;
    }

    public String getUsername() {
        return this.username;
    }

    public User setActive(Boolean active) {
        this.active = active;
        return this;
    }

    public Boolean getActive() {
        return this.active;
    }

    public User setSites(List<Site> sites) {
        this.sites = sites;
        return this;
    }

    public List<Site> getSites() {
        return this.sites;
    }

    public User setCustomerIdentity(String customerIdentity) {
        this.customerIdentity = customerIdentity;
        return this;
    }

    public String getCustomerIdentity() {
        return this.customerIdentity;
    }
}
