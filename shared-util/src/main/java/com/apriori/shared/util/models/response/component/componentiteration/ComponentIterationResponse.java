package com.apriori.shared.util.models.response.component.componentiteration;

import com.apriori.shared.util.annotations.Schema;

@Schema(location = "ComponentIterationsResponse.json")
public class ComponentIterationResponse {
    private ComponentIteration response;

    public ComponentIteration getResponse() {
        return this.response;
    }
}