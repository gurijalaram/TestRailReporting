package com.apriori.qms.entity.response.scenariodiscussion;

import com.apriori.utils.Pagination;
import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.List;

@Data
@JsonRootName("response")
@Schema(location = "ScenarioDiscussionsResponseSchema.json")
public class ScenarioDiscussionsResponse extends Pagination {
    List<ScenarioDiscussionResponse> items;
}
