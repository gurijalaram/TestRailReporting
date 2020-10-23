package com.apriori.apibase.services.response.objects;

import com.apriori.utils.http.enums.Schema;
import com.apriori.utils.json.deserializers.DateTimeDeserializer_yyyyMMddTHHmmZ;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.time.LocalDateTime;

@Schema(location = "BillOfMaterialSchema.json")
public class BillOfMaterial {

    @JsonProperty
    private Integer numberOfLineItemsCannotCost;

    @JsonProperty
    private Integer numberOfLineItemsNotCosted;

    @JsonProperty
    private Integer numberOfLineItemsCosted;

    @JsonProperty
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmZ.class)
    private LocalDateTime createdAt;

    @JsonProperty
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmZ.class)
    private LocalDateTime updatedAt;

    @JsonProperty
    private String createdBy;

    @JsonProperty
    private String identity;

    @JsonProperty
    private String name;

    @JsonProperty
    private Integer numberOfLineItemsReadyForExport;

    @JsonProperty
    private Integer totalNumberOfLineItems;

    @JsonProperty
    private Integer numberOfLineItemsNoPartsMatched;

    @JsonProperty
    private Integer numberOfLineItemsIncomplete;

    @JsonProperty
    private String filename;

    @JsonProperty
    private String type;


    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public BillOfMaterial setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public BillOfMaterial setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public String getIdentity() {
        return identity;
    }

    public BillOfMaterial setIdentity(String identity) {
        this.identity = identity;
        return this;
    }

    public String getName() {
        return name;
    }

    public BillOfMaterial setName(String name) {
        this.name = name;
        return this;
    }

    public Integer getNumberOfLineItemsReadyForExport() {
        return numberOfLineItemsReadyForExport;
    }

    public BillOfMaterial setNumberOfLineItemsReadyForExport(Integer numberOfLineItemsReadyForExport) {
        this.numberOfLineItemsReadyForExport = numberOfLineItemsReadyForExport;
        return this;
    }

    public Integer getTotalNumberOfLineItems() {
        return totalNumberOfLineItems;
    }

    public BillOfMaterial setTotalNumberOfLineItems(Integer totalNumberOfLineItems) {
        this.totalNumberOfLineItems = totalNumberOfLineItems;
        return this;
    }

    public Integer getNumberOfLineItemsCannotCost() {
        return numberOfLineItemsCannotCost;
    }

    public BillOfMaterial setNumberOfLineItemsCannotCost(Integer numberOfLineItemsCannotCost) {
        this.numberOfLineItemsCannotCost = numberOfLineItemsCannotCost;
        return this;
    }

    public Integer getNumberOfLineItemsNotCosted() {
        return numberOfLineItemsNotCosted;
    }

    public BillOfMaterial setNumberOfLineItemsNotCosted(Integer numberOfLineItemsNotCosted) {
        this.numberOfLineItemsNotCosted = numberOfLineItemsNotCosted;
        return this;
    }

    public Integer getNumberOfLineItemsCosted() {
        return numberOfLineItemsCosted;
    }

    public BillOfMaterial setNumberOfLineItemsCosted(Integer numberOfLineItemsCosted) {
        this.numberOfLineItemsCosted = numberOfLineItemsCosted;
        return this;
    }

    public String getFilename() {
        return filename;
    }

    public BillOfMaterial setFilename(String filename) {
        this.filename = filename;
        return this;
    }

    public String getType() {
        return type;
    }

    public BillOfMaterial setType(String type) {
        this.type = type;
        return this;
    }

    public Integer getNumberOfLineItemsNoPartsMatched() {
        return numberOfLineItemsNoPartsMatched;
    }

    public BillOfMaterial setNumberOfLineItemsNoPartsMatched(Integer numberOfLineItemsNoPartsMatched) {
        this.numberOfLineItemsNoPartsMatched = numberOfLineItemsNoPartsMatched;
        return this;
    }

    public Integer getNumberOfLineItemsIncomplete() {
        return numberOfLineItemsIncomplete;
    }

    public BillOfMaterial setNumberOfLineItemsIncomplete(Integer numberOfLineItemsIncomplete) {
        this.numberOfLineItemsIncomplete = numberOfLineItemsIncomplete;
        return this;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public BillOfMaterial setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }
}
