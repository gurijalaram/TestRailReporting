package com.apriori.apibase.services.cid.objects.response.publish.publishworkorderresult;

import com.apriori.utils.http.enums.Schema;

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
    "dateStarted",
    "dateCompleted"
})

@Schema(location = "cid/PublishWorkOrderResultSchema.json")
public class PublishWorkOrderInfoResult {

    @JsonProperty("version")
    private Integer version;
    @JsonProperty("id")
    private String id;
    @JsonProperty("status")
    private String status;
    @JsonProperty("searchKey")
    private String searchKey;
    @JsonProperty("command")
    private PublishResultCommand command;
    @JsonProperty("dateSubmitted")
    private String dateSubmitted;
    @JsonProperty("dateStarted")
    private String dateStarted;
    @JsonProperty("dateCompleted")
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
