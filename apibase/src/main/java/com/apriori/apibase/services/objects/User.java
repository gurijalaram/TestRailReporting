package com.apriori.apibase.services.objects;

import com.apriori.apibase.http.enums.Schema;
import com.apriori.utils.json.deserializers.DateTimeDeserializer_yyyyMMddTHHmmZ;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.time.LocalDateTime;
import java.util.List;

@Schema(location = "CdsUserSchema.json")
public class User {
    @JsonProperty
    private String identity;
    
    @JsonProperty
    private String createdBy;
    
    @JsonProperty
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmZ.class)
    private LocalDateTime createdAt;
    
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
    private List<Site> sites;

    @JsonProperty
    private String customerIdentity;
    
    @JsonProperty
    private User response;
    
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
