package main.java.http.builder.common.response.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import main.java.http.enums.Schema;


@Schema(location = "AccountsStatusSchema.json")
public class AccountsStatus extends CommonResponse {

    @JsonProperty
    private Boolean isValid;

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

    public AccountsStatus setValid(Boolean valid) {
        isValid = valid;
        return this;
    }

    public Boolean getActive() {
        return isActive;
    }

    public AccountsStatus setActive(Boolean active) {
        isActive = active;
        return this;
    }

    public String getIdentity() {
        return identity;
    }

    public AccountsStatus setIdentity(String identity) {
        this.identity = identity;
        return this;
    }

    public String getName() {
        return name;
    }

    public AccountsStatus setName(String name) {
        this.name = name;
        return this;
    }

    public String getType() {
        return type;
    }

    public AccountsStatus setType(String type) {
        this.type = type;
        return this;
    }

    public String getAccountId() {
        return accountId;
    }

    public AccountsStatus setAccountId(String accountId) {
        this.accountId = accountId;
        return this;
    }

    public String getAccountSecret() {
        return accountSecret;
    }

    public AccountsStatus setAccountSecret(String accountSecret) {
        this.accountSecret = accountSecret;
        return this;
    }

    public Integer getLicenseLimit() {
        return licenseLimit;
    }

    public AccountsStatus setLicenseLimit(Integer licenseLimit) {
        this.licenseLimit = licenseLimit;
        return this;
    }

    public Integer getLicenseUsage() {
        return licenseUsage;
    }

    public AccountsStatus setLicenseUsage(Integer licenseUsage) {
        this.licenseUsage = licenseUsage;
        return this;
    }

    public String getValidFrom() {
        return validFrom;
    }

    public AccountsStatus setValidFrom(String validFrom) {
        this.validFrom = validFrom;
        return this;
    }

    public String getValidTo() {
        return validTo;
    }

    public AccountsStatus setValidTo(String validTo) {
        this.validTo = validTo;
        return this;
    }
}
