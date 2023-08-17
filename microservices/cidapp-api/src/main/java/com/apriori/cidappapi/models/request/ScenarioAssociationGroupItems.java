package com.apriori.cidappapi.models.request;

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
