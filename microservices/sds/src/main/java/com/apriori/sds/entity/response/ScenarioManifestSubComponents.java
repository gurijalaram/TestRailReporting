package com.apriori.sds.entity.response;

import com.apriori.apibase.services.JacksonUtil;
import com.apriori.utils.http.enums.Schema;
import lombok.Data;

@Schema(location = "sds/ScenarioManifestSubComponents.json")
@Data
public class ScenarioManifestSubComponents extends JacksonUtil {
    private String occurrences;
    private String componentType;
    private String[] subComponents;
    private String componentName;
    private String componentIdentity;
    private String totalComponents;
    private String scenarioIdentity;
    private String scenarioName;
    private String scenarioState;
    private String totalSubComponents;
}
