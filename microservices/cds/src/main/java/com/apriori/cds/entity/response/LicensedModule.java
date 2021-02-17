package com.apriori.cds.entity.response;

import com.apriori.utils.json.deserializers.DateTimeDeserializer_yyyyMMddTHHmmZ;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class LicensedModule {
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmZ.class)
    private LocalDateTime createdAt;
    private String createdBy;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmZ.class)
    private LocalDateTime deletedAt;
    private String deletedBy;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmZ.class)
    private LocalDateTime expiresAt;
    private String identity;
    private String licenseSignature;
    private String name;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmZ.class)
    private LocalDateTime updatedAt;
    private String updatedBy;

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LicensedModule setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public LicensedModule setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public LicensedModule setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
        return this;
    }

    public String getDeletedBy() {
        return deletedBy;
    }

    public LicensedModule setDeletedBy(String deletedBy) {
        this.deletedBy = deletedBy;
        return this;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public LicensedModule setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
        return this;
    }

    public String getIdentity() {
        return identity;
    }

    public LicensedModule setIdentity(String identity) {
        this.identity = identity;
        return this;
    }

    public String getLicenseSignature() {
        return licenseSignature;
    }

    public LicensedModule setLicenseSignature(String licenseSignature) {
        this.licenseSignature = licenseSignature;
        return this;
    }

    public String getName() {
        return name;
    }

    public LicensedModule setName(String name) {
        this.name = name;
        return this;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public LicensedModule setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public LicensedModule setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
        return this;
    }
}
