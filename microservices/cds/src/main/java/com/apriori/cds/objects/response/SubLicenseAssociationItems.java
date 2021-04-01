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

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public List<UserSite> getSites() {
        return sites;
    }

    public void setSites(List<UserSite> sites) {
        this.sites = sites;
    }

    public CustomAttributes getCustomAttributes() {
        return customAttributes;
    }

    public void setCustomAttributes(CustomAttributes customAttributes) {
        this.customAttributes = customAttributes;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public Boolean getMfaRequired() {
        return mfaRequired;
    }

    public void setMfaRequired(Boolean mfaRequired) {
        this.mfaRequired = mfaRequired;
    }

    public String getCustomerIdentity() {
        return customerIdentity;
    }

    public void setCustomerIdentity(String customerIdentity) {
        this.customerIdentity = customerIdentity;
    }
}