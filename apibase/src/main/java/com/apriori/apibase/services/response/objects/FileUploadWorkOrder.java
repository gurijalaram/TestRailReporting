package com.apriori.apibase.services.response.objects;

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

@Schema(location = "FileUploadWorkOrderSchema.json")
public class FileUploadWorkOrder {

    @JsonProperty("version")
    private Integer version;
    @JsonProperty("id")
    private String id;
    @JsonProperty("status")
    private String status;
    @JsonProperty("searchKey")
    private String searchKey;
    @JsonProperty("command")
    private FileUploadCommand command;
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
    public void setVersion(Integer version) {
        this.version = version;
    }

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(String status) {
        this.status = status;
    }

    @JsonProperty("searchKey")
    public String getSearchKey() {
        return searchKey;
    }

    @JsonProperty("searchKey")
    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    @JsonProperty("command")
    public FileUploadCommand getCommand() {
        return command;
    }

    @JsonProperty("command")
    public void setCommand(FileUploadCommand command) {
        this.command = command;
    }

    @JsonProperty("dateSubmitted")
    public String getDateSubmitted() {
        return dateSubmitted;
    }

    @JsonProperty("dateSubmitted")
    public void setDateSubmitted(String dateSubmitted) {
        this.dateSubmitted = dateSubmitted;
    }

    @JsonProperty("dateStarted")
    public String getDateStarted() {
        return dateStarted;
    }

    @JsonProperty("dateStarted")
    public void setDateStarted(String dateStarted) {
        this.dateStarted = dateStarted;
    }

    @JsonProperty("dateCompleted")
    public String getDateCompleted() {
        return dateCompleted;
    }

    @JsonProperty("dateCompleted")
    public void setDateCompleted(String dateCompleted) {
        this.dateCompleted = dateCompleted;
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
