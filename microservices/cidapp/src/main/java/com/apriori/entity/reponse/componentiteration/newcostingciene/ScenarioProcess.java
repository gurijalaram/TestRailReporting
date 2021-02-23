package com.apriori.entity.reponse.componentiteration.newcostingciene;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ScenarioProcess {
    private String identity;
    private Double capitalInvestment;
    private Boolean costingFailed;
    private Double cycleTime;
    private String displayName;
    private Double fullyBurdenedCost;
    private String machineName;
    private Integer order;
    private String processGroupName;
    private String processName;
    private Double totalCost;
    private String vpeName;

    public String getIdentity() {
        return identity;
    }

    public ScenarioProcess setIdentity(String identity) {
        this.identity = identity;
        return this;
    }

    public Double getCapitalInvestment() {
        return capitalInvestment;
    }

    public ScenarioProcess setCapitalInvestment(Double capitalInvestment) {
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

    public Double getCycleTime() {
        return cycleTime;
    }

    public ScenarioProcess setCycleTime(Double cycleTime) {
        this.cycleTime = cycleTime;
        return this;
    }

    public String getDisplayName() {
        return displayName;
    }

    public ScenarioProcess setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public Double getFullyBurdenedCost() {
        return fullyBurdenedCost;
    }

    public ScenarioProcess setFullyBurdenedCost(Double fullyBurdenedCost) {
        this.fullyBurdenedCost = fullyBurdenedCost;
        return this;
    }

    public String getMachineName() {
        return machineName;
    }

    public ScenarioProcess setMachineName(String machineName) {
        this.machineName = machineName;
        return this;
    }

    public Integer getOrder() {
        return order;
    }

    public ScenarioProcess setOrder(Integer order) {
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

    public Double getTotalCost() {
        return totalCost;
    }

    public ScenarioProcess setTotalCost(Double totalCost) {
        this.totalCost = totalCost;
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
