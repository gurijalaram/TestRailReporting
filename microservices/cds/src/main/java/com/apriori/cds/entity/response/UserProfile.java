package com.apriori.cds.entity.response;

import com.apriori.utils.json.deserializers.DateTimeDeserializer_yyyyMMddTHHmmZ;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.time.LocalDateTime;

public class UserProfile {
    private String identity;
    private String createdBy;
    private String givenName;
    private String familyName;
    private String jobTitle;
    private String department;
    private String supervisor;
    private String timezone;
    private String townCity;
    private String countryCode;
    private String prefix;
    private String suffix;
    private String stateProvince;
    private String county;


    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmZ.class)
    private LocalDateTime createdAt;

    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmZ.class)
    private LocalDateTime updatedAt;

    public UserProfile setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public UserProfile setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public String getCounty() {
        return county;
    }

    public UserProfile setCounty(String county) {
        this.county = county;
        return this;
    }

    public String getStateProvince() {
        return stateProvince;
    }

    public UserProfile setStateProvince(String stateProvince) {
        this.stateProvince = stateProvince;
        return this;
    }

    public String getSuffix() {
        return suffix;
    }

    public UserProfile setSuffix(String suffix) {
        this.suffix = suffix;
        return this;
    }

    public String getPrefix() {
        return prefix;
    }

    public UserProfile setPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    public String getCountryCode() {
        return this.countryCode;
    }

    public UserProfile setCountryCode(String countryCode) {
        this.countryCode = countryCode;
        return this;
    }


    public UserProfile setTownCity(String townCity) {
        this.townCity = townCity;
        return this;
    }

    public String getTownCity() {
        return this.townCity;
    }

    public String getTimezone() {
        return timezone;
    }

    public UserProfile setTimezone(String timezone) {
        this.timezone = timezone;
        return this;
    }

    public String getSupervisor() {
        return this.supervisor;
    }

    public UserProfile setSupervisor(String supervisor) {
        this.supervisor = supervisor;
        return this;
    }

    public String getDepartment() {
        return this.department;
    }

    public UserProfile setDepartment(String department) {
        this.department = department;
        return this;
    }

    public String getJobTitle() {
        return this.jobTitle;
    }

    public UserProfile setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
        return this;
    }


    public UserProfile setIdentity(String identity) {
        this.identity = identity;
        return this;
    }

    public String getIdentity() {
        return this.identity;
    }

    public UserProfile setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }


    public UserProfile setGivenName(String givenName) {
        this.givenName = givenName;
        return this;
    }

    public String getGivenName() {
        return this.givenName;
    }

    public UserProfile setFamilyName(String familyName) {
        this.familyName = familyName;
        return this;
    }

    public String getFamilyName() {
        return this.familyName;
    }

}
