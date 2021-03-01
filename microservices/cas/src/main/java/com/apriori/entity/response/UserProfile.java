package com.apriori.entity.response;

import com.apriori.utils.json.deserializers.DateTimeDeserializer_yyyyMMddTHHmmZ;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserProfile {
    private String identity;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmZ.class)
    private LocalDateTime createdAt;
    private String createdBy;
    private String givenName;
    private String familyName;
    private String jobTitle;
    private String department;
    private String supervisor;
    private String townCity;

    public String getIdentity() {
        return identity;
    }

    public UserProfile setIdentity(String identity) {
        this.identity = identity;
        return this;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public UserProfile setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public UserProfile setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public String getGivenName() {
        return givenName;
    }

    public UserProfile setGivenName(String givenName) {
        this.givenName = givenName;
        return this;
    }

    public String getFamilyName() {
        return familyName;
    }

    public UserProfile setFamilyName(String familyName) {
        this.familyName = familyName;
        return this;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public UserProfile setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
        return this;
    }

    public String getDepartment() {
        return department;
    }

    public UserProfile setDepartment(String department) {
        this.department = department;
        return this;
    }

    public String getSupervisor() {
        return supervisor;
    }

    public UserProfile setSupervisor(String supervisor) {
        this.supervisor = supervisor;
        return this;
    }

    public String getTownCity() {
        return townCity;
    }

    public UserProfile setTownCity(String townCity) {
        this.townCity = townCity;
        return this;
    }
}
