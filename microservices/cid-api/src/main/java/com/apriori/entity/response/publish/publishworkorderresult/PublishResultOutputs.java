package com.apriori.entity.response.publish.publishworkorderresult;

import com.apriori.entity.response.upload.ScenarioIterationKey;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PublishResultOutputs {
    private ScenarioIterationKey scenarioIterationKey;
    private String comments;
    private String description;
}
