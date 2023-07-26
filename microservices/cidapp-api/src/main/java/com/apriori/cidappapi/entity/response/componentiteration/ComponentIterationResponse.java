package com.apriori.cidappapi.entity.response.componentiteration;

import com.apriori.annotations.Schema;

@Schema(location = "ComponentIterationsResponse.json")
public class ComponentIterationResponse {
    private ComponentIteration response;

    public ComponentIteration getResponse() {
        return this.response;
    }
}
