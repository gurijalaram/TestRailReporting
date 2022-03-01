package com.apriori.acs.entity.request.workorders.publish.createpublishworkorder;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PublishScenarioIterationKey {
    private PublishScenarioKey scenarioKey;
    private Integer iteration;
}