package main.java.http.builder.common.response.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import main.java.http.enums.Schema;
import main.java.json.deserializers.DateTimeDeserializer_yyyyMMddTHHmmssSSSZ;

import java.time.LocalDateTime;

@Schema(location = "BillOfMaterialSchema.json")
public class BillOfMaterial {

    @JsonProperty
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime createdAt;

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
}
