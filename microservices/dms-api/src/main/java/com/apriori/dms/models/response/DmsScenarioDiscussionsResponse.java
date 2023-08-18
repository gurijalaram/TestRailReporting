package com.apriori.dms.models.response;

import com.apriori.annotations.Schema;
import com.apriori.models.response.Pagination;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.List;

@Data
@JsonRootName("response")
@Schema(location = "DmsScenarioDiscussionsResponseSchema.json")
public class DmsScenarioDiscussionsResponse extends Pagination {
    List<DmsScenarioDiscussionResponse> items;
}