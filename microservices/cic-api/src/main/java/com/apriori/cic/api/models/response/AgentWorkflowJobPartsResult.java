package com.apriori.cic.api.models.response;

import com.apriori.shared.util.annotations.Schema;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

@Data
@JsonRootName("response")
@Schema(location = "AgentWorkflowJobPartResultSchema.json")
public class AgentWorkflowJobPartsResult {
    private String cidPartNumber;
    private String errorMessage;
    private String cicStatus;
    private String cidPartLink;
    private String apwScenarioLink;
    private String partId;
    private String partNumber;
    private String revisionNumber;
    private String description;
    private String scenarioName;
    private String costingResult;
    private String partType;
    private AgentWorkflowJobPartCostingInput input;
    private AgentWorkflowJobPartCostingResult result;
}