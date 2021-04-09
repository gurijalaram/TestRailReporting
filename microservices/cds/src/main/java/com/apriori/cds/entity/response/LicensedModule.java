package com.apriori.cds.entity.response;

import com.apriori.utils.json.deserializers.DateDeserializer_yyyyMMdd;
import com.apriori.utils.json.deserializers.DateTimeDeserializer_yyyyMMddTHHmmZ;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.time.LocalDate;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class LicensedModule {
    private String identity;
    private String createdBy;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmZ.class)
    private LocalDateTime createdAt;
    private String name;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateDeserializer_yyyyMMdd.class)
    private LocalDate expiresAt;
    private String licenseSignature;

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

    public String getIdentity() {
        return identity;
    }

    public LicensedModule setIdentity(String identity) {
        this.identity = identity;
        return this;
    }

    public String getName() {
        return name;
    }

    public LicensedModule setName(String name) {
        this.name = name;
        return this;
    }

    public LocalDate getExpiresAt() {
        return expiresAt;
    }

    public LicensedModule setExpiresAt(LocalDate expiresAt) {
        this.expiresAt = expiresAt;
        return this;
    }

    public String getLicenseSignature() {
        return licenseSignature;
    }

    public LicensedModule setLicenseSignature(String licenseSignature) {
        this.licenseSignature = licenseSignature;
        return this;
    }
}
