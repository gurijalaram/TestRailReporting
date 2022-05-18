package com.apriori.cidappapi.entity.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupItems {
    private String componentIdentity;
    private String scenarioIdentity;
    private String scenarioAssociationIdentity;
    private String childScenarioIdentity;
    private Boolean excluded;
    private Boolean included;
    private Integer occurrences;

}
