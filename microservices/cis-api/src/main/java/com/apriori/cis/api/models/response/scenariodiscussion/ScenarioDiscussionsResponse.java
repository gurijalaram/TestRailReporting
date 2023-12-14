package com.apriori.cis.api.models.response.scenariodiscussion;

import com.apriori.shared.util.annotations.Schema;
import com.apriori.shared.util.models.response.Pagination;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.List;

@Data
@JsonRootName("response")
@Schema(location = "ScenarioDiscussionsResponseSchema.json")
public class ScenarioDiscussionsResponse extends Pagination {
    List<ScenarioDiscussionResponse> items;
}
