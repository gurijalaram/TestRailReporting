package com.apriori.sds.entity.response;

import com.apriori.apibase.services.LombokUtil;
import com.apriori.utils.http.enums.Schema;

@Schema(location = "sds/ScenarioKey.json")
public class ScenarioKey extends LombokUtil {
    private String stateName;
    private String typeName;
    private String masterName;
    private Integer workspaceId;
}