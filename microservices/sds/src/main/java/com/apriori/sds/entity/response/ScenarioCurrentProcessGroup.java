package com.apriori.sds.entity.response;

import com.apriori.apibase.services.LombokUtil;
import com.apriori.utils.http.enums.Schema;

@Schema(location = "sds/ScenarioCurrentProcessGroup.json")
public class ScenarioCurrentProcessGroup extends LombokUtil {
    private String first;
    private String second;
}
