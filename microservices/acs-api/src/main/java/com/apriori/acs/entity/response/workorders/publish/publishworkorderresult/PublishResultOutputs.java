package com.apriori.acs.entity.response.workorders.publish.publishworkorderresult;

import com.apriori.acs.entity.response.workorders.upload.ScenarioIterationKey;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PublishResultOutputs {
    private ScenarioIterationKey scenarioIterationKey;
    private String comments;
    private String description;
}
