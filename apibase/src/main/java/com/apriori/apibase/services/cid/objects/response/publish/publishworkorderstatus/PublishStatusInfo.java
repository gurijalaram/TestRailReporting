package com.apriori.apibase.services.cid.objects.response.publish.publishworkorderstatus;

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
    "dateStarted"
})

@Schema(location = "cid/PublishWorkOrderStatusResponse.json")
public class PublishStatusInfo {

    @JsonProperty("version")
    private Integer version;
    @JsonProperty("id")
    private String id;
    @JsonProperty("status")
    private String status;
    @JsonProperty("searchKey")
    private String searchKey;
    @JsonProperty("command")
    private PublishStatusCommand command;
    @JsonProperty("dateSubmitted")
    private String dateSubmitted;
    @JsonProperty("dateStarted")
    private String dateStarted;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<>();

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

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
}