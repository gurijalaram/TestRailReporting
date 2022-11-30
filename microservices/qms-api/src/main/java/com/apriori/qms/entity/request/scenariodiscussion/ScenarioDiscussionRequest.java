package com.apriori.qms.entity.request.scenariodiscussion;

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
public class ScenarioDiscussionRequest {
    private ScenarioDiscussionParameters scenarioDiscussion;
}
