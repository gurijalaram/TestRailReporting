package com.apriori.cds.objects.response;

import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "SubLicenseAssociationUserSchema.json")
public class SubLicenseAssociationUser {
    private SubLicenseAssociationUser response;
    private String identity;
    private String createdBy;

    public SubLicenseAssociationUser getResponse() {
        return response;
    }

    public SubLicenseAssociationUser setResponse(SubLicenseAssociationUser response) {
        this.response = response;
        return this;
    }

    public String getIdentity() {
        return identity;
    }

    public SubLicenseAssociationUser setIdentity(String identity) {
        this.identity = identity;
        return this;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public SubLicenseAssociationUser setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }
}