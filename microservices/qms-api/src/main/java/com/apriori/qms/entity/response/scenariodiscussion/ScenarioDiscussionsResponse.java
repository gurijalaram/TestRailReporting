package com.apriori.qms.entity.response.scenariodiscussion;

import com.apriori.annotations.Schema;
import com.apriori.authorization.response.Pagination;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.List;

@Data
@JsonRootName("response")
@Schema(location = "ScenarioDiscussionsResponseSchema.json")
public class ScenarioDiscussionsResponse extends Pagination {
    List<ScenarioDiscussionResponse> items;
}
