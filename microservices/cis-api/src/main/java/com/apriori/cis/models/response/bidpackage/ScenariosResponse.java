package com.apriori.cis.models.response.bidpackage;

import com.apriori.annotations.Schema;
import com.apriori.cidappapi.models.response.scenarios.ScenarioResponse;
import com.apriori.models.response.Pagination;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.List;

@Data
@JsonRootName("response")
@Schema(location = "ScenarioDiscussionsResponseSchema.json")
public class ScenariosResponse extends Pagination {
    private List<ScenarioResponse> items;
}