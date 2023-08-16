package com.apriori.qms.models.request.scenariodiscussion;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScenarioDiscussionParameters {
    private String status;
    private String type;
    private String description;
    private String assigneeEmail;
    private String componentIdentity;
    private String scenarioIdentity;
    private Attributes attributes;
}
