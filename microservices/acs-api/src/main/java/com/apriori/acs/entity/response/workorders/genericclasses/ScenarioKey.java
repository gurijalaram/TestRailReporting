package com.apriori.acs.entity.response.workorders.genericclasses;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ScenarioKey {
    private String stateName;
    private String masterName;
    private String typeName;
    private Integer workspaceId;
}
