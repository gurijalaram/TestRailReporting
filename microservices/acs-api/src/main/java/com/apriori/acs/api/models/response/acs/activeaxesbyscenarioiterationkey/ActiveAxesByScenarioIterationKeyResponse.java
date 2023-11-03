package com.apriori.acs.api.models.response.acs.activeaxesbyscenarioiterationkey;

import com.apriori.shared.util.annotations.Schema;

import lombok.Data;

import java.util.List;

@Data
@Schema(location = "acs/ActiveAxesByScenarioIterationKeyResponse.json")
public class ActiveAxesByScenarioIterationKeyResponse {
    private List<ActiveAxesItem> itemList;
}
