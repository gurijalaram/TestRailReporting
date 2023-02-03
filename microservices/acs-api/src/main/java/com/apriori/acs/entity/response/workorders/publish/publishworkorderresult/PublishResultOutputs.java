package com.apriori.acs.entity.response.workorders.publish.publishworkorderresult;

import com.apriori.acs.entity.response.workorders.genericclasses.ScenarioIterationKey;
import com.apriori.acs.entity.response.workorders.upload.AssemblyComponent;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class PublishResultOutputs {
    private ScenarioIterationKey scenarioIterationKey;
    private String comments;
    private String description;
    private List<AssemblyComponent> subComponents;
    private List<UpdatedScenarioIteration> updatedScenarioIterations;
}
