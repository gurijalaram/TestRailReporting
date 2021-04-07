package com.apriori.sds.entity.response;

import com.apriori.apibase.services.LombokUtil;
import com.apriori.utils.http.enums.Schema;

@Schema(location = "sds/ScenarioResponse.json")
public class ScenarioResponse extends LombokUtil {
    private Scenario response;
}
