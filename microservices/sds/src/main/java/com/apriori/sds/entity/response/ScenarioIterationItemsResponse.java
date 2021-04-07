package com.apriori.sds.entity.response;

import com.apriori.apibase.services.Pagination;
import com.apriori.utils.http.enums.Schema;

import java.util.List;

@Schema(location = "sds/ScenarioIterationItemsResponse.json")
public class ScenarioIterationItemsResponse extends Pagination {
    private ScenarioIterationItemsResponse response;
    private List<ScenarioIteration> items;
}
