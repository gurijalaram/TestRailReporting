package com.apriori.acs.api.models.response.workorders.bomloader;

import com.apriori.shared.util.annotations.Schema;

import lombok.Data;

@Data
@Schema(location = "workorders/BomLoaderSchema.json")
public class BomLoaderResponse {
    private Integer version;
    private String id;
    private String priority;
    private String userId;
    private String status;
    private String searchKey;
    private Command command;
    private DateSubmitted dateSubmitted;
    private DateStarted dateStarted;
    private DateCompleted dateCompleted;
    private CommandType commandType;
    private Inputs inputs;
    private Outputs outputs;
    private ScenarioIterationKey scenarioIterationKey;
    private Mapping mapping;
    private ScenarioKey scenarioKey;
    private Iteration iteration;
    private MasterName masterName;
    private StateName stateName;
    private TypeName typeName;
    private WorkspaceId workspaceId;
    private Level level;
    private Occurrences occurrences;
}
