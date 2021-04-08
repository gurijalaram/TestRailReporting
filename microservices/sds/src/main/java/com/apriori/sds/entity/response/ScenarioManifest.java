package com.apriori.sds.entity.response;

import com.apriori.apibase.services.JacksonUtil;
import com.apriori.utils.http.enums.Schema;
import lombok.Data;

@Schema(location = "sds/ScenarioManifest.json")
@Data
public class ScenarioManifest extends JacksonUtil {
    private ScenarioManifest response;
    private String occurrences;
    private String componentType;
    private ScenarioManifestSubComponents[] subComponents;
    private String componentName;
    private String componentIdentity;
    private String cadMetadataIdentity;
    private String totalComponents;
    private String scenarioIdentity;
    private String scenarioName;
    private String scenarioState;
    private String totalSubComponents;
}
