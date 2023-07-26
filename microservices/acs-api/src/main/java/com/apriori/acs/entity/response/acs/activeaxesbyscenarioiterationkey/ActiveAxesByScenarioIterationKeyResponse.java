package com.apriori.acs.entity.response.acs.activeaxesbyscenarioiterationkey;

import com.apriori.annotations.Schema;

import lombok.Data;

import java.util.List;

@Data
@Schema(location = "acs/ActiveAxesByScenarioIterationKeyResponse.json")
public class ActiveAxesByScenarioIterationKeyResponse {
    private List<ActiveAxesItem> itemList;
}
