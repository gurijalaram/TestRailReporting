package com.apriori.apibase.http.builder.common.response.common;

import com.apriori.apibase.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@Schema(location = "WorkOrdersWrapperSchema.json")
public class WorkOrdersWrapper {

    @JsonProperty
    private List<WorkOrder> scenarioInfos;

    @JsonProperty
    private Integer totalAvailableCount;

    public Integer getTotalAvailableCount() {
        return totalAvailableCount;
    }

    public WorkOrdersWrapper setTotalAvailableCount(Integer totalAvailableCount) {
        this.totalAvailableCount = totalAvailableCount;
        return this;
    }

    public List<WorkOrder> getScenarioInfos() {
        return scenarioInfos;
    }

    public WorkOrdersWrapper setScenarioInfos(List<WorkOrder> scenarioInfos) {
        this.scenarioInfos = scenarioInfos;
        return this;
    }
}
