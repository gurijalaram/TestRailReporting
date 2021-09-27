package com.apriori.sds.entity.response;

import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

@Schema(location = "ScenarioKey.json")
@Data
@JsonRootName("response")
public class ScenarioKey {
    private String stateName;
    private String typeName;
    private String masterName;
    private Integer workspaceId;
}