package com.apriori.acs.entity.response.workorders.genericclasses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScenarioKey {
    private String stateName;
    private String masterName;
    private String typeName;
    private Integer workspaceId;
}
