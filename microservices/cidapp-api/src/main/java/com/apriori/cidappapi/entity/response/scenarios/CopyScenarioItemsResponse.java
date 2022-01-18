package com.apriori.cidappapi.entity.response.scenarios;

import com.apriori.apibase.services.Pagination;
import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.List;

@Schema(location = "CopyScenarioItemsResponse.json")
@Data
@JsonRootName("response")
public class CopyScenarioItemsResponse extends Pagination {
    private List<CopyScenarioResponse> items;
}
