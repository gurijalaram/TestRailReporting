package com.apriori.apibase.http.builder.common.response.common;

import com.apriori.apibase.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

@Schema(location = "ScenarioNameSchema.json")
public class ScenarioNameEntity {

    @JsonProperty
    private String scenarioName;

    public String getScenarioName() {
        return scenarioName;
    }

    public ScenarioNameEntity setScenarioName(String scenarioName) {
        this.scenarioName = scenarioName;
        return this;
    }

}
