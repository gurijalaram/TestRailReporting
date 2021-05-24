package com.apriori.cidapp.entity.response.componentiteration;

import com.apriori.utils.http.enums.Schema;

@Schema(location = "newcid/ComponentIterationsResponse.json")
public class ComponentIterationResponse {
    private ComponentIteration response;

    public ComponentIteration getResponse() {
        return this.response;
    }
}
