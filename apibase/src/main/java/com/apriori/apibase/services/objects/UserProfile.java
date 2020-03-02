package com.apriori.apibase.services.objects;

public class UserProfile {
    private String identity;
    private String createdBy;
    private String createdAt;
    private String givenName;
    private String familyName;


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
