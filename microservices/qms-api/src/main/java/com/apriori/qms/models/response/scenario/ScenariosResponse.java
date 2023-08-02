package com.apriori.qms.models.response.scenario;

import com.apriori.annotations.Schema;
import com.apriori.authorization.response.Pagination;
import com.apriori.cidappapi.models.response.scenarios.ScenarioResponse;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.List;

@Data
@JsonRootName("response")
@Schema(location = "ScenarioDiscussionsResponseSchema.json")
public class ScenariosResponse extends Pagination {
    private List<ScenarioResponse> items;
}