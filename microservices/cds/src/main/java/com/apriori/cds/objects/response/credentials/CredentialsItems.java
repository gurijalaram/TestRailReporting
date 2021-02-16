
package com.apriori.cds.objects.response.credentials;

import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@Schema(location = "cds/CredentialsSchema.json")
public class CredentialsItems {

    @JsonProperty
    private CredentialsItems response;
    @JsonProperty
    private String identity;
    @JsonProperty
    private String userIdentity;
    @JsonProperty
    private String email;
    @JsonProperty
    private String passwordHash;
    @JsonProperty
    private String passwordSalt;
    @JsonProperty
    private List<String> passwordHashHistory = null;
    @JsonProperty
    private List<String> passwordSaltHistory = null;

    public CredentialsItems getResponse() {
        return response;
    }

    public CredentialsItems setResponse(CredentialsItems response) {
        this.response = response;
        return this;
    }

    public String getIdentity() {
        return identity;
    }

    public CredentialsItems setIdentity(String identity) {
        this.identity = identity;
        return this;
    }

    public String getUserIdentity() {
        return userIdentity;
    }

    public CredentialsItems setUserIdentity(String userIdentity) {
        this.userIdentity = userIdentity;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public CredentialsItems setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public CredentialsItems setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
        return this;
    }

    public String getPasswordSalt() {
        return passwordSalt;
    }

    public CredentialsItems setPasswordSalt(String passwordSalt) {
        this.passwordSalt = passwordSalt;
        return this;
    }

    public List<String> getPasswordHashHistory() {
        return passwordHashHistory;
    }

    public CredentialsItems setPasswordHashHistory(List<String> passwordHashHistory) {
        this.passwordHashHistory = passwordHashHistory;
        return this;
    }

    public List<String> getPasswordSaltHistory() {
        return passwordSaltHistory;
    }

    public CredentialsItems setPasswordSaltHistory(List<String> passwordSaltHistory) {
        this.passwordSaltHistory = passwordSaltHistory;
        return this;
    }
}
