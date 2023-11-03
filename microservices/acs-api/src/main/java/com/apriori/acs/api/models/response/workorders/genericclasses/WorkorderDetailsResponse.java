package com.apriori.acs.api.models.response.workorders.genericclasses;

import com.apriori.shared.util.annotations.Schema;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "workorders/WorkorderDetailsResponse.json")
public class WorkorderDetailsResponse {
    private Integer version;
    private String id;
    private String priority;
    private String status;
    private String userId;
    private String searchKey;
    private Command command;
    private String dateSubmitted;
    private String dateStarted;
    private String dateCompleted;

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    public Command getCommand() {
        return command;
    }

    public void setCommand(Command command) {
        this.command = command;
    }

    public String getDateSubmitted() {
        return dateSubmitted;
    }

    public void setDateSubmitted(String dateSubmitted) {
        this.dateSubmitted = dateSubmitted;
    }

    public String getDateStarted() {
        return dateStarted;
    }

    public void setDateStarted(String dateStarted) {
        this.dateStarted = dateStarted;
    }

    public String getDateCompleted() {
        return dateCompleted;
    }

    public void setDateCompleted(String dateCompleted) {
        this.dateCompleted = dateCompleted;
    }

    public class Command {
        private String commandType;
        private Object inputs;
        private Object outputs;

        public String getCommandType() {
            return commandType;
        }

        public void setCommandType(String commandType) {
            this.commandType = commandType;
        }

        public Object getInputs() {
            return inputs;
        }

        public void setInputs(Object inputs) {
            this.inputs = inputs;
        }

        public Object getOutputs() {
            return outputs;
        }

        public void setOutputs(Object outputs) {
            this.outputs = outputs;
        }
    }

}
