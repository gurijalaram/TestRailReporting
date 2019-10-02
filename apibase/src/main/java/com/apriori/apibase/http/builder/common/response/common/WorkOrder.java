package com.apriori.apibase.http.builder.common.response.common;

import com.apriori.apibase.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@Schema(location = "WorkOrderSchema.json")
@JsonIgnoreProperties(ignoreUnknown = true)
public class WorkOrder {

    @JsonProperty
    private String version;

    @JsonProperty
    private String id;

    @JsonProperty
    private String status;

    @JsonProperty
    private String searchKey;

    @JsonProperty
    private WorkOrderCommand command;

    @JsonProperty
    private String dateSubmitted;

    @JsonProperty
    private ScenarioIterationKey scenarioIterationKey;

    public ScenarioIterationKey getScenarioIterationKey() {
        return scenarioIterationKey;
    }

    public WorkOrder setScenarioIterationKey(ScenarioIterationKey scenarioIterationKey) {
        this.scenarioIterationKey = scenarioIterationKey;
        return this;
    }

    public String getVersion() {
        return version;
    }

    public WorkOrder setVersion(String version) {
        this.version = version;
        return this;
    }

    public String getId() {
        return id;
    }

    public WorkOrder setId(String id) {
        this.id = id;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public WorkOrder setStatus(String status) {
        this.status = status;
        return this;
    }

    public String getSearchKey() {
        return searchKey;
    }

    public WorkOrder setSearchKey(String searchKey) {
        this.searchKey = searchKey;
        return this;
    }

    public WorkOrderCommand getCommand() {
        return command;
    }

    public WorkOrder setCommand(WorkOrderCommand command) {
        this.command = command;
        return this;
    }

    public String getDateSubmitted() {
        return dateSubmitted;
    }

    public WorkOrder setDateSubmitted(String dateSubmitted) {
        this.dateSubmitted = dateSubmitted;
        return this;
    }
}
