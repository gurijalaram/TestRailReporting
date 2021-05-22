package com.apriori.entity.request.publish.createpublishworkorder;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PublishScenarioKey {
    private String typeName;
    private String stateName;
    private Integer workspaceId;
    private String masterName;
}
