package com.apriori.sds.entity.response;

import com.apriori.apibase.services.Pagination;
import com.apriori.utils.http.enums.Schema;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Schema(location = "sds/ScenarioItemsResponse.json")
@Data
@EqualsAndHashCode(callSuper = true)
public class ComponentsItemsResponse extends Pagination {
    private ComponentsItemsResponse response;
    private List<Component> items;
}
