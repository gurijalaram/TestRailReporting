package com.apriori.acs.entity.response.acs.createmissingscenario;

import lombok.Data;

@Data
public class ScenarioKey {
    private String stateName;
    private String masterName;
    private String typeName;
    private Integer workspaceId;
}