package com.apriori.apibase.services.response.objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;


@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
@JsonTypeName(value = "command")
public class WorkOrderCommand {
    @JsonProperty
    private String commandType;

    @JsonProperty
    private WorkOrderInputs inputs;

    public String getCommandType() {
        return commandType;
    }

    public WorkOrderCommand setCommandType(String commandType) {
        this.commandType = commandType;
        return this;
    }

    public WorkOrderInputs getInputs() {
        return inputs;
    }

    public WorkOrderCommand setInputs(WorkOrderInputs inputs) {
        this.inputs = inputs;
        return this;
    }
}
