package com.apriori.entity.reponse.componentiteration;

import com.apriori.utils.json.deserializers.DateTimeDeserializer_yyyyMMddTHHmmZ;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ScenarioCustomAttribute {
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmZ.class)
    private LocalDateTime createdAt;
    private String createdBy;
    private String createdByName;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmZ.class)
    private LocalDateTime deletedAt;
    private String deletedBy;
    private String deletedByName;
    private String identity;
    private String key;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmZ.class)
    private LocalDateTime updatedAt;
    private String updatedBy;
    private String updatedByName;
    private String value;
    private String valueType;

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public ScenarioCustomAttribute setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public ScenarioCustomAttribute setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public String getCreatedByName() {
        return createdByName;
    }

    public ScenarioCustomAttribute setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
        return this;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public ScenarioCustomAttribute setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
        return this;
    }

    public String getDeletedBy() {
        return deletedBy;
    }

    public ScenarioCustomAttribute setDeletedBy(String deletedBy) {
        this.deletedBy = deletedBy;
        return this;
    }

    public String getDeletedByName() {
        return deletedByName;
    }

    public ScenarioCustomAttribute setDeletedByName(String deletedByName) {
        this.deletedByName = deletedByName;
        return this;
    }

    public String getIdentity() {
        return identity;
    }

    public ScenarioCustomAttribute setIdentity(String identity) {
        this.identity = identity;
        return this;
    }

    public String getKey() {
        return key;
    }

    public ScenarioCustomAttribute setKey(String key) {
        this.key = key;
        return this;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public ScenarioCustomAttribute setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public ScenarioCustomAttribute setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
        return this;
    }

    public String getUpdatedByName() {
        return updatedByName;
    }

    public ScenarioCustomAttribute setUpdatedByName(String updatedByName) {
        this.updatedByName = updatedByName;
        return this;
    }

    public String getValue() {
        return value;
    }

    public ScenarioCustomAttribute setValue(String value) {
        this.value = value;
        return this;
    }

    public String getValueType() {
        return valueType;
    }

    public ScenarioCustomAttribute setValueType(String valueType) {
        this.valueType = valueType;
        return this;
    }
}
