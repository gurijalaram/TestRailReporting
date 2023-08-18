package com.apriori.cic.models.response;

import com.apriori.annotations.Schema;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Schema(location = "AgentWorkflowJobResultSchema.json")
public class AgentWorkflowJobResults extends ArrayList<AgentWorkflowJobPartsResult> {
    private List<AgentWorkflowJobPartsResult> results;
}