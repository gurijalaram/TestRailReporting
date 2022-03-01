package com.apriori.acs.entity.response.acs.getactiveaxesbyscenarioiterationkey;

import com.apriori.utils.http.enums.Schema;

import lombok.Data;

import java.util.List;

@Data
@Schema(location = "acs/GetActiveAxesByScenarioIterationKeyResponse.json")
public class GetActiveAxesByScenarioIterationKeyResponse {
    private List<ActiveAxesItem> itemList;
}
