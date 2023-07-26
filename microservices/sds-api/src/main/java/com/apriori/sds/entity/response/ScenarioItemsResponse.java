package com.apriori.sds.entity.response;

import com.apriori.annotations.Schema;
import com.apriori.authorization.response.Pagination;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Schema(location = "ScenarioItemsResponse.json")
@Data
@EqualsAndHashCode(callSuper = true)
@JsonRootName("response")
public class ScenarioItemsResponse extends Pagination {
    private List<Scenario> items;
}
