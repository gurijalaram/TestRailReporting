package com.apriori.shared.util.models.response.component.componentiteration;

import lombok.Data;

@Data
public class ScenarioKey {
    private String typeName;
    private String stateName;
    private Integer workspaceId;
    private String masterName;
}
