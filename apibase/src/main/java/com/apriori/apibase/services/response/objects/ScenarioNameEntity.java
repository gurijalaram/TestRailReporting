package com.apriori.apibase.services.response.objects;



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
