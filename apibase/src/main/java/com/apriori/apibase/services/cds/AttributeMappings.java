package com.apriori.apibase.services.cds;

public class AttributeMappings {
    private String name;
    private String email;
    private String userId;
    private String givenName;
    private String familyName;

    public String getName() {
        return name;
    }

    public AttributeMappings setName(String name) {
        this.name = name;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public AttributeMappings setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getUserId() {
        return userId;
    }

    public AttributeMappings setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public String getGivenName() {
        return givenName;
    }

    public AttributeMappings setGivenName(String givenName) {
        this.givenName = givenName;
        return this;
    }

    public String getFamilyName() {
        return familyName;
    }

    public AttributeMappings setFamilyName(String familyName) {
        this.familyName = familyName;
        return this;
    }
}
