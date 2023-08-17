package com.apriori.acs.models.response.workorders.cost.costworkorderstatus;

public class CostOrderStatusInfo {
    private Integer version;
    private String id;
    private String status;
    private String searchKey;
    private CostStatusCommand command;
    private String dateSubmitted;
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