package com.apriori.entity.response.scenarios;

import com.apriori.utils.http.enums.Schema;
import com.apriori.utils.json.deserializers.DateTimeDeserializer_yyyyMMddTHHmmZ;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "cidapp/CostResponseSchema.json")
public class CostResponse {
    private CostResponse response;
    private String identity;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmZ.class)
    private LocalDateTime createdAt;
    private String createdBy;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmZ.class)
    private LocalDateTime updatedAt;
    private String customerIdentity;
    private String scenarioName;
    private String scenarioType;
    private String scenarioState;
    private String previousScenarioState;
    private String lastAction;
    private String ownedBy;
    private Boolean locked;
    private Boolean published;
    private String ownedByName;
    private String createdByName;

    public CostResponse getResponse() {
        return response;
    }

    public String getIdentity() {
        return identity;
    }

    public CostResponse setIdentity(String identity) {
        this.identity = identity;
        return this;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public CostResponse setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public CostResponse setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public CostResponse setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public String getCustomerIdentity() {
        return customerIdentity;
    }

    public CostResponse setCustomerIdentity(String customerIdentity) {
        this.customerIdentity = customerIdentity;
        return this;
    }

    public String getScenarioName() {
        return scenarioName;
    }

    public CostResponse setScenarioName(String scenarioName) {
        this.scenarioName = scenarioName;
        return this;
    }

    public String getScenarioType() {
        return scenarioType;
    }

    public CostResponse setScenarioType(String scenarioType) {
        this.scenarioType = scenarioType;
        return this;
    }

    public String getScenarioState() {
        return scenarioState;
    }

    public CostResponse setScenarioState(String scenarioState) {
        this.scenarioState = scenarioState;
        return this;
    }

    public String getPreviousScenarioState() {
        return previousScenarioState;
    }

    public CostResponse setPreviousScenarioState(String previousScenarioState) {
        this.previousScenarioState = previousScenarioState;
        return this;
    }

    public String getLastAction() {
        return lastAction;
    }

    public CostResponse setLastAction(String lastAction) {
        this.lastAction = lastAction;
        return this;
    }

    public String getOwnedBy() {
        return ownedBy;
    }

    public CostResponse setOwnedBy(String ownedBy) {
        this.ownedBy = ownedBy;
        return this;
    }

    public Boolean getLocked() {
        return locked;
    }

    public CostResponse setLocked(Boolean locked) {
        this.locked = locked;
        return this;
    }

    public Boolean getPublished() {
        return published;
    }

    public CostResponse setPublished(Boolean published) {
        this.published = published;
        return this;
    }

    public String getOwnedByName() {
        return ownedByName;
    }

    public CostResponse setOwnedByName(String ownedByName) {
        this.ownedByName = ownedByName;
        return this;
    }

    public String getCreatedByName() {
        return createdByName;
    }

    public CostResponse setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
        return this;
    }
}
