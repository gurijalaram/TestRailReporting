package com.apriori.sds.entity.response;

import com.apriori.apibase.services.LombokUtil;
import com.apriori.utils.http.enums.Schema;

@Schema(location = "sds/ScenarioIterationResponse.json")
public class ScenarioIterationResponse extends LombokUtil {
    private ScenarioIteration response;
}
