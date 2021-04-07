package com.apriori.sds.entity.response;

import com.apriori.apibase.services.Pagination;
import com.apriori.utils.http.enums.Schema;

import java.util.List;

@Schema(location = "sds/ScenarioItemsResponse.json")
public class ScenarioItemsResponse extends Pagination {
    private ScenarioItemsResponse response;
    private List<Scenario> items;
}
