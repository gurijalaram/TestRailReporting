package com.apriori.sds.entity.response;

import com.apriori.annotations.Schema;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

@Schema(location = "ScenarioAvailableProcessGroupSelection.json")
@Data
@JsonRootName("response")
public class ScenarioAvailableProcessGroupSelection {
    private String manuallyCosted;
    private String displayName;
    private String pgName;
}
