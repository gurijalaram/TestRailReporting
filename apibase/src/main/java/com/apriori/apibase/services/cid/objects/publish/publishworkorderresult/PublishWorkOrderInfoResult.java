package com.apriori.apibase.services.cid.objects.publish.publishworkorderresult;

import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.HashMap;
import java.util.Map;

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
    private PublishCommand command;
    @JsonProperty("dateSubmitted")
    private String dateSubmitted;
    @JsonProperty("dateStarted")
    private String dateStarted;
    @JsonProperty("dateCompleted")
    private String dateCompleted;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("version")
    public Integer getVersion() {
        return version;
    }

    @JsonProperty("version")
    public PublishWorkOrderInfoResult setVersion(Integer version) {
        this.version = version;
        return this;
    }

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public PublishWorkOrderInfoResult setId(String id) {
        this.id = id;
        return this;
    }

    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    @JsonProperty("status")
    public PublishWorkOrderInfoResult setStatus(String status) {
        this.status = status;
        return this;
    }

    @JsonProperty("searchKey")
    public String getSearchKey() {
        return searchKey;
    }

    @JsonProperty("searchKey")
    public PublishWorkOrderInfoResult setSearchKey(String searchKey) {
        this.searchKey = searchKey;
        return this;
    }

    @JsonProperty("command")
    public PublishCommand getCommand() {
        return command;
    }

    @JsonProperty("command")
    public PublishWorkOrderInfoResult setCommand(PublishCommand command) {
        this.command = command;
        return this;
    }

    @JsonProperty("dateSubmitted")
    public String getDateSubmitted() {
        return dateSubmitted;
    }

    @JsonProperty("dateSubmitted")
    public PublishWorkOrderInfoResult setDateSubmitted(String dateSubmitted) {
        this.dateSubmitted = dateSubmitted;
        return this;
    }

    @JsonProperty("dateStarted")
    public String getDateStarted() {
        return dateStarted;
    }

    @JsonProperty("dateStarted")
    public PublishWorkOrderInfoResult setDateStarted(String dateStarted) {
        this.dateStarted = dateStarted;
        return this;
    }

    @JsonProperty("dateCompleted")
    public String getDateCompleted() {
        return dateCompleted;
    }

    @JsonProperty("dateCompleted")
    public PublishWorkOrderInfoResult setDateCompleted(String dateCompleted) {
        this.dateCompleted = dateCompleted;
        return this;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
}
