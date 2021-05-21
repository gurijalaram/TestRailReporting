package com.apriori.cds.objects.response;

import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "cds/SubLicenseAssociationUsersSchema.json")
public class SubLicenseAssociationItems {
    private String identity;
    private String createdBy;
    private String createdAt;
    private UserProfile userProfile;
    private String email;
    private String username;
    private Boolean active;
    private List<UserSite> sites = null;
    private CustomAttributes customAttributes;
    private String userType;
    private Boolean mfaRequired;
    private String customerIdentity;

    public String getIdentity() {
        return identity;
    }

    public SubLicenseAssociationItems setIdentity(String identity) {
        this.identity = identity;
        return this;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public SubLicenseAssociationItems setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public SubLicenseAssociationItems setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public SubLicenseAssociationItems setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public SubLicenseAssociationItems setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public SubLicenseAssociationItems setUsername(String username) {
        this.username = username;
        return this;
    }

    public Boolean getActive() {
        return active;
    }

    public SubLicenseAssociationItems setActive(Boolean active) {
        this.active = active;
        return this;
    }

    public List<UserSite> getSites() {
        return sites;
    }

    public SubLicenseAssociationItems setSites(List<UserSite> sites) {
        this.sites = sites;
        return this;
    }

    public CustomAttributes getCustomAttributes() {
        return customAttributes;
    }

    public SubLicenseAssociationItems setCustomAttributes(CustomAttributes customAttributes) {
        this.customAttributes = customAttributes;
        return this;
    }

    public String getUserType() {
        return userType;
    }

    public SubLicenseAssociationItems setUserType(String userType) {
        this.userType = userType;
        return this;
    }

    public Boolean getMfaRequired() {
        return mfaRequired;
    }

    public SubLicenseAssociationItems setMfaRequired(Boolean mfaRequired) {
        this.mfaRequired = mfaRequired;
        return this;
    }

    public String getCustomerIdentity() {
        return customerIdentity;
    }

    public SubLicenseAssociationItems setCustomerIdentity(String customerIdentity) {
        this.customerIdentity = customerIdentity;
        return this;
    }
}