package com.apriori.apibase.services.sds;

import com.apriori.utils.http.enums.Schema;
import com.fasterxml.jackson.annotation.JsonProperty;

@Schema(location = "sds/ScenarioCostingDefaults.json")
public class ScenarioCostingDefaults {

    private ScenarioCostingDefaultsResponse response;

    public ScenarioCostingDefaultsResponse getResponse () {
        return response;
    }

    public void setResponse (ScenarioCostingDefaultsResponse response) {
        this.response = response;
    }
}
