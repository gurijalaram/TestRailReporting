package com.apriori.acs.models.response.workorders.upload;

import com.apriori.acs.models.response.workorders.genericclasses.ScenarioIterationKey;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AssemblyComponent {
    private Boolean ignored;
    private ScenarioIterationKey scenarioIterationKey;
}
