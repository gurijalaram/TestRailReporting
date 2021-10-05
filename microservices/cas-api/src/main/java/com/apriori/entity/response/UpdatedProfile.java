package com.apriori.entity.response;

import com.apriori.utils.json.deserializers.DateTimeDeserializer_yyyyMMddTHHmmssSSSZ;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdatedProfile {
    private String identity;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime createdAt;
    private String createdBy;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime updatedAt;
    private String givenName;
    private String familyName;
    private String jobTitle;
    private String department;
    private String supervisor;

    public String getIdentity() {
        return identity;
    }

    public UpdatedProfile setIdentity(String identity) {
        this.identity = identity;
        return this;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public UpdatedProfile setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public UpdatedProfile setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public UpdatedProfile setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public String getGivenName() {
        return givenName;
    }

    public UpdatedProfile setGivenName(String givenName) {
        this.givenName = givenName;
        return this;
    }

    public String getFamilyName() {
        return familyName;
    }

    public UpdatedProfile setFamilyName(String familyName) {
        this.familyName = familyName;
        return this;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public UpdatedProfile setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
        return this;
    }

    public String getDepartment() {
        return department;
    }

    public UpdatedProfile setDepartment(String department) {
        this.department = department;
        return this;
    }

    public String getSupervisor() {
        return supervisor;
    }

    public UpdatedProfile setSupervisor(String supervisor) {
        this.supervisor = supervisor;
        return this;
    }
}
