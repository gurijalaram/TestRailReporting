package com.apriori.sds.entity.response;

import com.apriori.annotations.Schema;
import com.apriori.authorization.response.Pagination;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Schema(location = "ScenarioIterationItemsResponse.json")
@Data
@EqualsAndHashCode(callSuper = true)
@JsonRootName("response")
public class ScenarioIterationItemsResponse extends Pagination {
    private List<ScenarioIteration> items;
}
