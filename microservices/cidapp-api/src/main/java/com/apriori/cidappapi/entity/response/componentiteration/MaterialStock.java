package com.apriori.cidappapi.entity.response.componentiteration;

import com.apriori.utils.json.deserializers.DateTimeDeserializer_yyyyMMddTHHmmssSSSZ;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MaterialStock {
    private String identity;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime createdAt;
    private String createdBy;
    private Double costPerUnit;
    private String costUnits;
    private String description;
    private String formName;
    private Double hardness;
    private String hardnessSystem;
    private Double length;
    private String name;
    private Boolean virtual;
    private Double width;

    public String getIdentity() {
        return identity;
    }

    public MaterialStock setIdentity(String identity) {
        this.identity = identity;
        return this;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public MaterialStock setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public MaterialStock setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public Double getCostPerUnit() {
        return costPerUnit;
    }

    public MaterialStock setCostPerUnit(Double costPerUnit) {
        this.costPerUnit = costPerUnit;
        return this;
    }

    public String getCostUnits() {
        return costUnits;
    }

    public MaterialStock setCostUnits(String costUnits) {
        this.costUnits = costUnits;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public MaterialStock setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getFormName() {
        return formName;
    }

    public MaterialStock setFormName(String formName) {
        this.formName = formName;
        return this;
    }

    public Double getHardness() {
        return hardness;
    }

    public MaterialStock setHardness(Double hardness) {
        this.hardness = hardness;
        return this;
    }

    public String getHardnessSystem() {
        return hardnessSystem;
    }

    public MaterialStock setHardnessSystem(String hardnessSystem) {
        this.hardnessSystem = hardnessSystem;
        return this;
    }

    public Double getLength() {
        return length;
    }

    public MaterialStock setLength(Double length) {
        this.length = length;
        return this;
    }

    public String getName() {
        return name;
    }

    public MaterialStock setName(String name) {
        this.name = name;
        return this;
    }

    public Boolean getVirtual() {
        return virtual;
    }

    public MaterialStock setVirtual(Boolean virtual) {
        this.virtual = virtual;
        return this;
    }

    public Double getWidth() {
        return width;
    }

    public MaterialStock setWidth(Double width) {
        this.width = width;
        return this;
    }
}
