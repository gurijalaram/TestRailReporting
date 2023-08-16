package com.apriori.cic.models.response;

import com.apriori.annotations.Schema;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

@Data
@JsonRootName("response")
@Schema(location = "AgentWorkflowSchema.json")
public class AgentWorkflow {
    private String name;
    private String description;
    private String id;
    private boolean locked;
    private String partSelectionType;
}
