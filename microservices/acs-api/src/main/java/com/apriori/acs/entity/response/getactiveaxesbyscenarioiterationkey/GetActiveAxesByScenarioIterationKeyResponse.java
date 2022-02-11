package com.apriori.acs.entity.response.getactiveaxesbyscenarioiterationkey;

import com.apriori.utils.http.enums.Schema;

import lombok.Data;

import java.util.List;

@Data
@Schema(location = "GetActiveAxesByScenarioIterationKeyResponse.json")
public class GetActiveAxesByScenarioIterationKeyResponse {
    private List<ActiveAxesItem> itemList;
}
