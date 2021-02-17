package com.apriori.apibase.services.sds;

import java.util.List;

import com.apriori.apibase.services.Pagination;
import com.apriori.utils.http.enums.Schema;

@Schema(location = "sds/ComponentsForScenario.json")
public class ComponentsForScenario extends Pagination {

    private ComponentsForScenario response;
    private List<Scenario> items;

    public ComponentsForScenario setResponse(ComponentsForScenario response) {
        this.response = response;
        return this;
    }

    public List<Scenario> getItems() {
        return items;
    }
}
