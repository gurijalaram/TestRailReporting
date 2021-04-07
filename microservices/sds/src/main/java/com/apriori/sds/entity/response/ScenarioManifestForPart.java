package com.apriori.sds.entity.response;

import com.apriori.apibase.services.LombokUtil;
import com.apriori.utils.http.enums.Schema;

@Schema(location = "sds/ScenarioManifestForPart.json")
public class ScenarioManifestForPart extends LombokUtil {
    private String path;
    private String error;
    private String message;
    private String timestamp;
    private String status;
}
