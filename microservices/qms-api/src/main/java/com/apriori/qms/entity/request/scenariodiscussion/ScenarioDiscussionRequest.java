package com.apriori.qms.entity.request.scenariodiscussion;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScenarioDiscussionRequest {
    private ScenarioDiscussionParameters scenarioDiscussion;
}
