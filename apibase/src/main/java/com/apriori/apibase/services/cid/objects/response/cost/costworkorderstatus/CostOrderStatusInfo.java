package com.apriori.apibase.services.cid.objects.response.cost.costworkorderstatus;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "version",
    "id",
    "status",
    "searchKey",
    "command",
    "dateSubmitted",
    "dateStarted"
})

public class CostOrderStatusInfo {

    @JsonProperty("version")
    private Integer version;
    @JsonProperty("id")
    private String id;
    @JsonProperty("status")
    private String status;
    @JsonProperty("searchKey")
    private String searchKey;
    @JsonProperty("command")
    private CostStatusCommand command;
    @JsonProperty("dateSubmitted")
    private String dateSubmitted;
    @JsonProperty("dateStarted")
    private String dateStarted;

    public Integer getVersion() {
        return version;
    }

    public CostOrderStatusInfo setVersion(Integer version) {
        this.version = version;
        return this;
    }

    public String getId() {
        return id;
    }

    public CostOrderStatusInfo setId(String id) {
        this.id = id;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public CostOrderStatusInfo setStatus(String status) {
        this.status = status;
        return this;
    }

    public String getSearchKey() {
        return searchKey;
    }

    public CostOrderStatusInfo setSearchKey(String searchKey) {
        this.searchKey = searchKey;
        return this;
    }

    public CostStatusCommand getCommand() {
        return command;
    }

    public CostOrderStatusInfo setCommand(CostStatusCommand command) {
        this.command = command;
        return this;
    }

    public String getDateSubmitted() {
        return dateSubmitted;
    }

    public CostOrderStatusInfo setDateSubmitted(String dateSubmitted) {
        this.dateSubmitted = dateSubmitted;
        return this;
    }

    public String getDateStarted() {
        return dateStarted;
    }

    public CostOrderStatusInfo setDateStarted(String dateStarted) {
        this.dateStarted = dateStarted;
        return this;
    }
}