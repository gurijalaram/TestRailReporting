package com.apriori.api.entity.reponse.componentiteration;

import com.apriori.utils.json.deserializers.DateTimeDeserializer_yyyyMMddTHHmmZ;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ScenarioProcess {
    private Integer capitalInvestment;
    private Boolean costingFailed;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmZ.class)
    private LocalDateTime createdAt;
    private String createdBy;
    private String createdByName;
    private Integer cycleTime;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmZ.class)
    private LocalDateTime deletedAt;
    private String deletedBy;
    private String deletedByName;
    private String displayName;
    private Integer fullyBurdenedCost;
    private String identity;
    private String machineName;
    private String optional;
    private String order;
    private String processGroupName;
    private String processName;
    private Integer totalCost;
    private String updatedAt;
    private String updatedBy;
    private String updatedByName;
    private Boolean userIncluded;
    private String vpeName;

    public Integer getCapitalInvestment() {
        return capitalInvestment;
    }

    public ScenarioProcess setCapitalInvestment(Integer capitalInvestment) {
        this.capitalInvestment = capitalInvestment;
        return this;
    }

    public Boolean getCostingFailed() {
        return costingFailed;
    }

    public ScenarioProcess setCostingFailed(Boolean costingFailed) {
        this.costingFailed = costingFailed;
        return this;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public ScenarioProcess setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public ScenarioProcess setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public String getCreatedByName() {
        return createdByName;
    }

    public ScenarioProcess setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
        return this;
    }

    public Integer getCycleTime() {
        return cycleTime;
    }

    public ScenarioProcess setCycleTime(Integer cycleTime) {
        this.cycleTime = cycleTime;
        return this;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public ScenarioProcess setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
        return this;
    }

    public String getDeletedBy() {
        return deletedBy;
    }

    public ScenarioProcess setDeletedBy(String deletedBy) {
        this.deletedBy = deletedBy;
        return this;
    }

    public String getDeletedByName() {
        return deletedByName;
    }

    public ScenarioProcess setDeletedByName(String deletedByName) {
        this.deletedByName = deletedByName;
        return this;
    }

    public String getDisplayName() {
        return displayName;
    }

    public ScenarioProcess setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public Integer getFullyBurdenedCost() {
        return fullyBurdenedCost;
    }

    public ScenarioProcess setFullyBurdenedCost(Integer fullyBurdenedCost) {
        this.fullyBurdenedCost = fullyBurdenedCost;
        return this;
    }

    public String getIdentity() {
        return identity;
    }

    public ScenarioProcess setIdentity(String identity) {
        this.identity = identity;
        return this;
    }

    public String getMachineName() {
        return machineName;
    }

    public ScenarioProcess setMachineName(String machineName) {
        this.machineName = machineName;
        return this;
    }

    public String getOptional() {
        return optional;
    }

    public ScenarioProcess setOptional(String optional) {
        this.optional = optional;
        return this;
    }

    public String getOrder() {
        return order;
    }

    public ScenarioProcess setOrder(String order) {
        this.order = order;
        return this;
    }

    public String getProcessGroupName() {
        return processGroupName;
    }

    public ScenarioProcess setProcessGroupName(String processGroupName) {
        this.processGroupName = processGroupName;
        return this;
    }

    public String getProcessName() {
        return processName;
    }

    public ScenarioProcess setProcessName(String processName) {
        this.processName = processName;

        return this;
    }

    public Integer getTotalCost() {
        return totalCost;
    }

    public ScenarioProcess setTotalCost(Integer totalCost) {
        this.totalCost = totalCost;
        return this;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public ScenarioProcess setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public ScenarioProcess setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
        return this;
    }

    public String getUpdatedByName() {
        return updatedByName;
    }

    public ScenarioProcess setUpdatedByName(String updatedByName) {
        this.updatedByName = updatedByName;
        return this;
    }

    public Boolean getUserIncluded() {
        return userIncluded;
    }

    public ScenarioProcess setUserIncluded(Boolean userIncluded) {
        this.userIncluded = userIncluded;
        return this;
    }

    public String getVpeName() {
        return vpeName;
    }

    public ScenarioProcess setVpeName(String vpeName) {
        this.vpeName = vpeName;
        return this;
    }
}
