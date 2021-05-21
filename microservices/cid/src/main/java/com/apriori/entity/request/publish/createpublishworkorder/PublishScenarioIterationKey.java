package com.apriori.entity.request.publish.createpublishworkorder;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PublishScenarioIterationKey {
    private PublishScenarioKey scenarioKey;
    private Integer iteration;
}