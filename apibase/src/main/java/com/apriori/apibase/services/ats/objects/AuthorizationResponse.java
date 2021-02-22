package com.apriori.apibase.services.ats.objects;

import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

@Schema(location = "AtsAuthorizeSchema.json")
public class AuthorizationResponse {
    @JsonProperty
    private AuthorizationResponse response;

    @JsonProperty
    private String identity;

    @JsonProperty
    private String createdBy;

    @JsonProperty
    private LocalDateTime createdAt;

    @JsonProperty
    private LocalDateTime updatedAt;

    @JsonProperty
    private String customerIdentity;

    @JsonProperty
    private String firstName;

    @JsonProperty
    private String lastName;

    @JsonProperty
    private String username;

    @JsonProperty
    private Object[] userDeployments;

    public AuthorizationResponse getResponse() {
        return response;
    }

    public AuthorizationResponse setResponse(AuthorizationResponse response) {
        this.response = response;
        return this;
    }

    public String getIdentity() {
        return identity;
    }

    public AuthorizationResponse setIdentity(String identity) {
        this.identity = identity;
        return this;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public AuthorizationResponse setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public AuthorizationResponse setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public AuthorizationResponse setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public String getCustomerIdentity() {
        return customerIdentity;
    }

    public AuthorizationResponse setCustomerIdentity(String customerIdentity) {
        this.customerIdentity = customerIdentity;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public AuthorizationResponse setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public AuthorizationResponse setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public AuthorizationResponse setUsername(String username) {
        this.username = username;
        return this;
    }

    public Object[] getUserDeployments() {
        return userDeployments;
    }

    public AuthorizationResponse setUserDeployments(Object[] userDeployments) {
        this.userDeployments = userDeployments;
        return this;
    }
}
