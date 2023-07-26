package com.apriori.apibase.services.response.objects;



import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@Schema(location = "FileWorkorderSchema.json")
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
    private WorkOrderScenarioIteration workOrderScenarioIteration;

    public WorkOrderScenarioIteration getWorkOrderScenarioIteration() {
        return workOrderScenarioIteration;
    }

    public WorkOrder setWorkOrderScenarioIteration(WorkOrderScenarioIteration workOrderScenarioIteration) {
        this.workOrderScenarioIteration = workOrderScenarioIteration;
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
