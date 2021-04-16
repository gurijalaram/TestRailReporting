package com.apriori.sds.entity.response;

import com.apriori.apibase.services.Pagination;
import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Schema(location = "sds/ScenarioIterationItemsResponse.json")
@Data
@EqualsAndHashCode(callSuper = true)
@JsonRootName("response")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ScenarioIterationItemsResponse extends Pagination {
    private List<ScenarioIteration> items;
}
