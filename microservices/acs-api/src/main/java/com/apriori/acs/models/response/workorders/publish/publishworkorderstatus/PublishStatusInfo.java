package com.apriori.acs.models.response.workorders.publish.publishworkorderstatus;

import com.apriori.annotations.Schema;

@Schema(location = "workorders/PublishWorkorderStatusResponse.json")
public class PublishStatusInfo {
    private Integer version;
    private String id;
    private String status;
    private String searchKey;
    private PublishStatusCommand command;
    private String dateSubmitted;
    private String dateStarted;

    public Integer getVersion() {
        return version;
    }

    public PublishStatusInfo setVersion(Integer version) {
        this.version = version;
        return this;
    }

    public String getId() {
        return id;
    }

    public PublishStatusInfo setId(String id) {
        this.id = id;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public PublishStatusInfo setStatus(String status) {
        this.status = status;
        return this;
    }

    public String getSearchKey() {
        return searchKey;
    }

    public PublishStatusInfo setSearchKey(String searchKey) {
        this.searchKey = searchKey;
        return this;
    }

    public PublishStatusCommand getCommand() {
        return command;
    }

    public PublishStatusInfo setCommand(PublishStatusCommand command) {
        this.command = command;
        return this;
    }

    public String getDateSubmitted() {
        return dateSubmitted;
    }

    public PublishStatusInfo setDateSubmitted(String dateSubmitted) {
        this.dateSubmitted = dateSubmitted;
        return this;
    }

    public String getDateStarted() {
        return dateStarted;
    }

    public PublishStatusInfo setDateStarted(String dateStarted) {
        this.dateStarted = dateStarted;
        return this;
    }
}