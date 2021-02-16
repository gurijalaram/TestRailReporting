package com.apriori.cds.entity.response;

import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "cds/CustomerAssociationSchema.json")
public class CustomerAssociationItems {
    private String createdAt;
    private String createdBy;
    private String deletedAt;
    private String deletedBy;
    private String description;
    private String identity;
    private String targetCustomerIdentity;
    private String type;
    private String updatedAt;
    private String updatedBy;
    private String response;

    public String getResponse() {
        return response;
    }

    public CustomerAssociationItems setResponse(String response) {
        this.response = response;
        return this;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public CustomerAssociationItems setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public CustomerAssociationItems setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public String getDeletedAt() {
        return deletedAt;
    }

    public CustomerAssociationItems setDeletedAt(String deletedAt) {
        this.deletedAt = deletedAt;
        return this;
    }

    public String getDeletedBy() {
        return deletedBy;
    }

    public CustomerAssociationItems setDeletedBy(String deletedBy) {
        this.deletedBy = deletedBy;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public CustomerAssociationItems setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getIdentity() {
        return identity;
    }

    public CustomerAssociationItems setIdentity(String identity) {
        this.identity = identity;
        return this;
    }

    public String getTargetCustomerIdentity() {
        return targetCustomerIdentity;
    }

    public CustomerAssociationItems setTargetCustomerIdentity(String targetCustomerIdentity) {
        this.targetCustomerIdentity = targetCustomerIdentity;
        return this;
    }

    public String getType() {
        return type;
    }

    public CustomerAssociationItems setType(String type) {
        this.type = type;
        return this;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public CustomerAssociationItems setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public CustomerAssociationItems setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
        return this;
    }
}