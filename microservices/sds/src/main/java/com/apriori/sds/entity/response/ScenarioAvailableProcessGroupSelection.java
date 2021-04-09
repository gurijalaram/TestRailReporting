package com.apriori.sds.entity.response;

import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonRootName;

import lombok.Data;

@Schema(location = "sds/ScenarioAvailableProcessGroupSelection.json")
@Data
@JsonRootName("response")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ScenarioAvailableProcessGroupSelection {
    private String manuallyCosted;
    private String displayName;
    private String pgName;
}
