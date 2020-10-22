package com.apriori.apibase.services.response.objects;

import com.apriori.utils.http.enums.Schema;
import com.apriori.utils.json.deserializers.DateTimeDeserializer_yyyyMMddTHHmmZ;
import com.apriori.utils.json.deserializers.DateTimeDeserializer_yyyyMMddTHHmmssSSSZ;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.time.LocalDateTime;
import java.util.List;

@Schema(location = "MaterialLineItemSchema.json")
public class MaterialLineItem {

    @JsonProperty
    private String identity;

    @JsonProperty
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmZ.class)
    private LocalDateTime createdAt;

    @JsonProperty
    private String createdBy;

    @JsonProperty
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmZ.class)
    private LocalDateTime updatedAt;

    @JsonProperty
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmZ.class)
    private LocalDateTime deletedAt;

    @JsonProperty
    private String deletedBy;

    @JsonProperty
    private String costingStatus;

    @JsonProperty
    private String status;

    @JsonProperty
    private String customerPartNumber;

    @JsonProperty
    private String manufacturerPartNumber;

    @JsonProperty
    private String manufacturer;

    @JsonProperty
    private String quantity;

    @JsonProperty
    private Integer level;

    @JsonProperty("parts")
    private List<MaterialPart> materialParts;

    public String getIdentity() {
        return identity;
    }

    public MaterialLineItem setIdentity(String identity) {
        this.identity = identity;
        return this;
    }

    public Integer getLevel() {
        return level;
    }

    public MaterialLineItem setLevel(Integer level) {
        this.level = level;
        return this;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public MaterialLineItem setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public MaterialLineItem setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public String getCostingStatus() {
        return costingStatus;
    }

    public MaterialLineItem setCostingStatus(String costingStatus) {
        this.costingStatus = costingStatus;
        return this;
    }

    public String getCustomerPartNumber() {
        return customerPartNumber;
    }

    public MaterialLineItem setCustomerPartNumber(String customerPartNumber) {
        this.customerPartNumber = customerPartNumber;
        return this;
    }

    public String getManufacturerPartNumber() {
        return manufacturerPartNumber;
    }

    public MaterialLineItem setManufacturerPartNumber(String manufacturerPartNumber) {
        this.manufacturerPartNumber = manufacturerPartNumber;
        return this;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public MaterialLineItem setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
        return this;
    }

    public String getQuantity() {
        return quantity;
    }

    public MaterialLineItem setQuantity(String quantity) {
        this.quantity = quantity;
        return this;
    }

    public List<MaterialPart> getMaterialParts() {
        return materialParts;
    }

    public MaterialLineItem setMaterialParts(List<MaterialPart> materialParts) {
        this.materialParts = materialParts;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public MaterialLineItem setStatus(String status) {
        this.status = status;
        return this;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public MaterialLineItem setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public MaterialLineItem setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
        return this;
    }

    public String getDeletedBy() {
        return deletedBy;
    }

    public MaterialLineItem setDeletedBy(String deletedBy) {
        this.deletedBy = deletedBy;
        return this;
    }
}
