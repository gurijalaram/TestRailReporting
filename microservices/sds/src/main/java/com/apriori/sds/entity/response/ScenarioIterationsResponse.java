package com.apriori.sds.entity.response;

import com.apriori.apibase.services.Pagination;
import com.apriori.utils.http.enums.Schema;

import java.util.List;

@Schema(location = "sds/ScenarioIterationsResponse.json")
public class ScenarioIterationsResponse extends Pagination {

    private ScenarioIterationsResponse response;
    private List<ScenarioIterations> items;

    public ScenarioIterationsResponse setResponse(ScenarioIterationsResponse response) {
        this.response = response;
        return this;
    }

    public List<ScenarioIterations> getItems() {
        return items;
    }
}
