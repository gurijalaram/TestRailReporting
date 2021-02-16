package com.apriori.cds.entity.response;

import com.apriori.utils.http.enums.Schema;
import com.apriori.utils.json.deserializers.DateTimeDeserializer_yyyyMMddTHHmmZ;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "cds/CustomerAssociationSchema.json")
public class CustomerAssociationItems {
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmZ.class)
    private LocalDateTime createdAt;
    private String createdBy;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmZ.class)
    private LocalDateTime deletedAt;
    private String deletedBy;
    private String description;
    private String identity;
    private String targetCustomerIdentity;
    private String type;
    private String updatedAt;
    private String updatedBy;
    private CustomerAssociationItems response;

    public CustomerAssociationItems getResponse() {
        return response;
    }

    public CustomerAssociationItems setResponse(CustomerAssociationItems response) {
        this.response = response;
        return this;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public CustomerAssociationItems setCreatedAt(LocalDateTime createdAt) {
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

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public CustomerAssociationItems setDeletedAt(LocalDateTime deletedAt) {
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