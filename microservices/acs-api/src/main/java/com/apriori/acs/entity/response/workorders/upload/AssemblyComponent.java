package com.apriori.acs.entity.response.workorders.upload;

import com.apriori.acs.entity.response.workorders.genericclasses.ScenarioIterationKey;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AssemblyComponent {
    private Boolean ignored;
    private ScenarioIterationKey scenarioIterationKey;
}
