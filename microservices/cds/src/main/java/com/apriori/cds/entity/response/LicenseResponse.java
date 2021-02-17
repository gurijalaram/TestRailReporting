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
    private Boolean active;
    private String apVersion;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmZ.class)
    private LocalDateTime createdAt;
    private String createdBy;
    private String customerIdentity;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmZ.class)
    private LocalDateTime deletedAt;
    private String deletedBy;
    private String description;
    private String identity;
    private List<LicensedModule> licensedModules = null;
    private String siteIdentity;
    private List<SubLicense> subLicenses = null;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmZ.class)
    private LocalDateTime updatedAt;
    private String updatedBy;

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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LicenseResponse setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public LicenseResponse setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public String getCustomerIdentity() {
        return customerIdentity;
    }

    public LicenseResponse setCustomerIdentity(String customerIdentity) {
        this.customerIdentity = customerIdentity;
        return this;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public LicenseResponse setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
        return this;
    }

    public String getDeletedBy() {
        return deletedBy;
    }

    public LicenseResponse setDeletedBy(String deletedBy) {
        this.deletedBy = deletedBy;
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

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public LicenseResponse setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public LicenseResponse setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
        return this;
    }
}
