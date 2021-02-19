package com.apriori.apibase.services.sds;

import com.apriori.utils.http.enums.Schema;

@Schema(location = "sds/ScenarioRepresentation.json")
public class ScenarioRepresentation {
    private ScenarioRepresentation response;

    public ScenarioRepresentation getResponse() {
        return response;
    }

    public void setResponse(ScenarioRepresentation response) {
        this.response = response;
    }
}
