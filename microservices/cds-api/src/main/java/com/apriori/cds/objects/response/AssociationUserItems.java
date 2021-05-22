package com.apriori.cds.objects.response;

import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "cds/AssociationUserSchema.json")
public class AssociationUserItems {
    private String identity;
    private String createdBy;
    private String createdAt;
    private String userIdentity;
    private AssociationUserItems response;

    public AssociationUserItems getResponse() {
        return response;
    }

    public AssociationUserItems setResponse(AssociationUserItems response) {
        this.response = response;
        return this;
    }

    public String getIdentity() {
        return identity;
    }

    public AssociationUserItems setIdentity(String identity) {
        this.identity = identity;
        return this;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public AssociationUserItems setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public AssociationUserItems setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public String getUserIdentity() {
        return userIdentity;
    }

    public AssociationUserItems setUserIdentity(String userIdentity) {
        this.userIdentity = userIdentity;
        return this;
    }
}