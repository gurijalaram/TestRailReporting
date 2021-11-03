package com.apriori.entity.request.publish.createpublishworkorder;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PublishInputs {
    private Boolean overwrite;
    private Boolean lock;
    private PublishScenarioIterationKey scenarioIterationKey;
    private String description;
    private String comments;
}
