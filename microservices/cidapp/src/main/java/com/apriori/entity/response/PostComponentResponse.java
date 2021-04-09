package com.apriori.entity.response;

import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "cidapp/PostComponentResponse.json")
public class PostComponentResponse {
    private String iterationIdentity;
    private String componentIdentity;
    private String scenarioIdentity;

    public String getIterationIdentity() {
        return iterationIdentity;
    }

    public String getComponentIdentity() {
        return componentIdentity;
    }

    public String getScenarioIdentity() {
        return scenarioIdentity;
    }
}
