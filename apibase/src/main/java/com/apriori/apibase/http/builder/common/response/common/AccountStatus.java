package com.apriori.apibase.http.builder.common.response.common;

import com.apriori.apibase.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;


@Schema(location = "AccountsStatusSchema.json")
public class AccountStatus {

    @JsonProperty
    private boolean isValid;

    @JsonProperty
    private Boolean isActive;

    @JsonProperty
    private String identity;

    @JsonProperty
    private String name;

    @JsonProperty
    private String type;

    @JsonProperty
    private String accountId;

    @JsonProperty
    private String accountSecret;

    @JsonProperty
    private Integer licenseLimit;

    @JsonProperty
    private Integer licenseUsage;

    @JsonProperty
    private String validFrom;

    @JsonProperty
    private String validTo;

    public Boolean getValid() {
        return isValid;
    }

    public Boolean getActive() {
        return isActive;
    }

    public AccountStatus setActive(Boolean active) {
        isActive = active;
        return this;
    }

    public String getIdentity() {
        return identity;
    }

    public AccountStatus setIdentity(String identity) {
        this.identity = identity;
        return this;
    }

    public String getName() {
        return name;
    }

    public AccountStatus setName(String name) {
        this.name = name;
        return this;
    }

    public String getType() {
        return type;
    }

    public AccountStatus setType(String type) {
        this.type = type;
        return this;
    }

    public String getAccountId() {
        return accountId;
    }

    public AccountStatus setAccountId(String accountId) {
        this.accountId = accountId;
        return this;
    }

    public String getAccountSecret() {
        return accountSecret;
    }

    public AccountStatus setAccountSecret(String accountSecret) {
        this.accountSecret = accountSecret;
        return this;
    }

    public Integer getLicenseLimit() {
        return licenseLimit;
    }

    public AccountStatus setLicenseLimit(Integer licenseLimit) {
        this.licenseLimit = licenseLimit;
        return this;
    }

    public Integer getLicenseUsage() {
        return licenseUsage;
    }

    public AccountStatus setLicenseUsage(Integer licenseUsage) {
        this.licenseUsage = licenseUsage;
        return this;
    }

    public String getValidFrom() {
        return validFrom;
    }

    public AccountStatus setValidFrom(String validFrom) {
        this.validFrom = validFrom;
        return this;
    }

    public String getValidTo() {
        return validTo;
    }

    public AccountStatus setValidTo(String validTo) {
        this.validTo = validTo;
        return this;
    }

    public boolean isValid() {
        return isValid;
    }

    public AccountStatus setValid(Boolean valid) {
        isValid = valid;
        return this;
    }

    public AccountStatus setValid(boolean valid) {
        isValid = valid;
        return this;
    }
}
