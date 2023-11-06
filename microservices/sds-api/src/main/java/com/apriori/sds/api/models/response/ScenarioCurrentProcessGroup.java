package com.apriori.sds.api.models.response;

import com.apriori.shared.util.annotations.Schema;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

@Schema(location = "ScenarioCurrentProcessGroup.json")
@Data
@JsonRootName("response")
public class ScenarioCurrentProcessGroup {
    private String first;
    private String second;
}
