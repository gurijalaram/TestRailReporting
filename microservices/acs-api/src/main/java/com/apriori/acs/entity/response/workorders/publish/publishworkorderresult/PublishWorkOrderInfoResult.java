package com.apriori.acs.entity.response.workorders.publish.publishworkorderresult;

import com.apriori.annotations.Schema;

@Schema(location = "workorders/PublishWorkorderResultResponse.json")
public class PublishWorkOrderInfoResult {
    private Integer version;
    private String id;
    private String status;
    private String searchKey;
    private PublishResultCommand command;
    private String dateSubmitted;
    private String dateStarted;
    private String dateCompleted;

    public Integer getVersion() {
        return version;
    }

    public PublishWorkOrderInfoResult setVersion(Integer version) {
        this.version = version;
        return this;
    }

    public String getId() {
        return id;
    }

    public PublishWorkOrderInfoResult setId(String id) {
        this.id = id;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public PublishWorkOrderInfoResult setStatus(String status) {
        this.status = status;
        return this;
    }

    public String getSearchKey() {
        return searchKey;
    }

    public PublishWorkOrderInfoResult setSearchKey(String searchKey) {
        this.searchKey = searchKey;
        return this;
    }

    public PublishResultCommand getCommand() {
        return command;
    }

    public PublishWorkOrderInfoResult setCommand(PublishResultCommand command) {
        this.command = command;
        return this;
    }

    public String getDateSubmitted() {
        return dateSubmitted;
    }

    public PublishWorkOrderInfoResult setDateSubmitted(String dateSubmitted) {
        this.dateSubmitted = dateSubmitted;
        return this;
    }

    public String getDateStarted() {
        return dateStarted;
    }

    public PublishWorkOrderInfoResult setDateStarted(String dateStarted) {
        this.dateStarted = dateStarted;
        return this;
    }

    public String getDateCompleted() {
        return dateCompleted;
    }

    public PublishWorkOrderInfoResult setDateCompleted(String dateCompleted) {
        this.dateCompleted = dateCompleted;
        return this;
    }
}
