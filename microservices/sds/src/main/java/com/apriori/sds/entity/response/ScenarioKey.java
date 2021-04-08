package com.apriori.sds.entity.response;

import com.apriori.apibase.services.JacksonUtil;
import com.apriori.utils.http.enums.Schema;
import lombok.Data;

@Schema(location = "sds/ScenarioKey.json")
@Data
public class ScenarioKey extends JacksonUtil {
    private String stateName;
    private String typeName;
    private String masterName;
    private Integer workspaceId;
}