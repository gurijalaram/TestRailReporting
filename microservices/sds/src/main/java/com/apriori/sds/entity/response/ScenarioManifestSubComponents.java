package com.apriori.sds.entity.response;

import com.apriori.apibase.services.LombokUtil;
import com.apriori.utils.http.enums.Schema;

@Schema(location = "sds/ScenarioManifestSubComponents.json")
public class ScenarioManifestSubComponents extends LombokUtil {
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
