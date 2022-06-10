package com.apriori.cidappapi.entity.request;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ScenarioAssociationGroupItems {
    private String scenarioAssociationIdentity;
    private String childScenarioIdentity;
    private Boolean excluded;
    private Integer occurrences;
}
