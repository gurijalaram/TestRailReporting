package com.apriori.entity.reponse.upload;

import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "newcid/UploadComponentResponse.json")
public class UploadComponentResponse {
    private String componentIdentity;
    private String scenarioIdentity;

    public String getComponentIdentity() {
        return componentIdentity;
    }

    public UploadComponentResponse setComponentIdentity(String componentIdentity) {
        this.componentIdentity = componentIdentity;
        return this;
    }

    public String getScenarioIdentity() {
        return scenarioIdentity;
    }
}
