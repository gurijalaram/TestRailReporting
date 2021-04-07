package com.apriori.sds.entity.response;

import com.apriori.apibase.services.Pagination;
import com.apriori.utils.http.enums.Schema;

import java.util.List;

@Schema(location = "sds/ScenarioItemsResponse.json")
public class ComponentsItemsResponse extends Pagination {
    private ComponentsItemsResponse response;
    private List<Component> items;
}
