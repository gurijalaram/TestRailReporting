package com.apriori.apibase.services.objects;

public class UserProfile {
    private String identity;
    private String createdBy;
    private String createdAt;
    private String givenName;
    private String familyName;
    private String jobTitle;
    private String department;
    private String supervisor;
    private String timezone;
    private String townCity;

    public String getTowncity() {
        return townCity;
    }

    public UserProfile setTownCity(String townCity) {
        this.townCity = townCity;
        return this;
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

    public UserProfile setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public String getCreatedAt() {
        return this.createdAt;
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
