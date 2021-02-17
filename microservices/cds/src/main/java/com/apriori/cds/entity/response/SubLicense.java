package com.apriori.cds.entity.response;

import com.apriori.utils.json.deserializers.DateTimeDeserializer_yyyyMMddTHHmmZ;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SubLicense {
    private String identity;
    private String createdBy;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmZ.class)
    private LocalDateTime createdAt;
    private String name;
    private String uuid;
    private List<String> licensedModuleNames = null;
    private Integer maxNumUsers;
    private Integer numPurchasedUsers;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmZ.class)
    private LocalDateTime expiresAt;
    private String licenseSignature;
    private Integer numAssignedUsers;

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public SubLicense setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public SubLicense setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public String getIdentity() {
        return identity;
    }

    public SubLicense setIdentity(String identity) {
        this.identity = identity;
        return this;
    }

    public String getLicenseSignature() {
        return licenseSignature;
    }

    public SubLicense setLicenseSignature(String licenseSignature) {
        this.licenseSignature = licenseSignature;
        return this;
    }

    public List<String> getLicensedModuleNames() {
        return licensedModuleNames;
    }

    public SubLicense setLicensedModuleNames(List<String> licensedModuleNames) {
        this.licensedModuleNames = licensedModuleNames;
        return this;
    }

    public Integer getMaxNumUsers() {
        return maxNumUsers;
    }

    public SubLicense setMaxNumUsers(Integer maxNumUsers) {
        this.maxNumUsers = maxNumUsers;
        return this;
    }

    public String getName() {
        return name;
    }

    public SubLicense setName(String name) {
        this.name = name;
        return this;
    }

    public Integer getNumAssignedUsers() {
        return numAssignedUsers;
    }

    public SubLicense setNumAssignedUsers(Integer numAssignedUsers) {
        this.numAssignedUsers = numAssignedUsers;
        return this;
    }

    public Integer getNumPurchasedUsers() {
        return numPurchasedUsers;
    }

    public SubLicense setNumPurchasedUsers(Integer numPurchasedUsers) {
        this.numPurchasedUsers = numPurchasedUsers;
        return this;
    }

    public String getUuid() {
        return uuid;
    }

    public SubLicense setUuid(String uuid) {
        this.uuid = uuid;
        return this;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public SubLicense setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
        return this;
    }
}
