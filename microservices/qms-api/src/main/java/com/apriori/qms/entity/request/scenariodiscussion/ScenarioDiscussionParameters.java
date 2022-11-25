package com.apriori.qms.entity.request.scenariodiscussion;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScenarioDiscussionParameters {
    private String status;
    private String type;
    private String description;
    private String componentIdentity;
    private String scenarioIdentity;
    private Attributes attributes;
}
