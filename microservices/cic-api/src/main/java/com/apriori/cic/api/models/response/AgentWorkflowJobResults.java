package com.apriori.cic.api.models.response;

import com.apriori.shared.util.annotations.Schema;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Schema(location = "AgentWorkflowJobResultSchema.json")
public class AgentWorkflowJobResults extends ArrayList<AgentWorkflowJobPartsResult> {
    private List<AgentWorkflowJobPartsResult> results;
}
