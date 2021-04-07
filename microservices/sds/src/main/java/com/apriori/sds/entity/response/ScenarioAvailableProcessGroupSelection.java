package com.apriori.sds.entity.response;

import com.apriori.apibase.services.LombokUtil;
import com.apriori.utils.http.enums.Schema;

@Schema(location = "sds/ScenarioAvailableProcessGroupSelection.json")
public class ScenarioAvailableProcessGroupSelection extends LombokUtil {
    private String manuallyCosted;
    private String displayName;
    private String pgName;
}
