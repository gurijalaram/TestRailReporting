package com.apriori.sds.entity.response;

import com.apriori.utils.Pagination;
import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.List;

@Schema(location = "ComponentsForScenario.json")
@Data
@JsonRootName("response")
public class ComponentsForScenario extends Pagination {
    private List<Scenario> items;
}
