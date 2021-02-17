package com.apriori.cds.entity.response;

import com.apriori.utils.http.enums.Schema;
import com.apriori.utils.json.deserializers.DateTimeDeserializer_yyyyMMddTHHmmZ;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "cds/LicenseResponseSchema.json")
public class LicenseResponse {
    private LicenseResponse response;
    private String identity;
    private String createdBy;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmZ.class)
    private LocalDateTime createdAt;
    private Boolean active;
    private String apVersion;
    private String description;
    private List<SubLicense> subLicenses = null;
    private List<LicensedModule> licensedModules = null;
    private String customerIdentity;
    private String siteIdentity;

    public LicenseResponse getResponse() {
        return response;
    }

    public LicenseResponse setResponse(LicenseResponse response) {
        this.response = response;
        return this;
    }

    public Boolean getActive() {
        return active;
    }

    public LicenseResponse setActive(Boolean active) {
        this.active = active;
        return this;
    }

    public String getApVersion() {
        return apVersion;
    }

    public LicenseResponse setApVersion(String apVersion) {
        this.apVersion = apVersion;
        return this;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public LicenseResponse setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LicenseResponse setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public String getCustomerIdentity() {
        return customerIdentity;
    }

    public LicenseResponse setCustomerIdentity(String customerIdentity) {
        this.customerIdentity = customerIdentity;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public LicenseResponse setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getIdentity() {
        return identity;
    }

    public LicenseResponse setIdentity(String identity) {
        this.identity = identity;
        return this;
    }

    public List<LicensedModule> getLicensedModules() {
        return licensedModules;
    }

    public LicenseResponse setLicensedModules(List<LicensedModule> licensedModules) {
        this.licensedModules = licensedModules;
        return this;
    }

    public String getSiteIdentity() {
        return siteIdentity;
    }

    public LicenseResponse setSiteIdentity(String siteIdentity) {
        this.siteIdentity = siteIdentity;
        return this;
    }

    public List<SubLicense> getSubLicenses() {
        return subLicenses;
    }

    public LicenseResponse setSubLicenses(List<SubLicense> subLicenses) {
        this.subLicenses = subLicenses;
        return this;
    }
}
