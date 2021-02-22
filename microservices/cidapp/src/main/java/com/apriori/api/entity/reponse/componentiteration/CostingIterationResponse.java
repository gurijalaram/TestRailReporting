package com.apriori.api.entity.reponse.componentiteration;

import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

@Schema(location = "newcid/ComponentIterationsResponse.json")
public class CostingIterationResponse {
    @JsonProperty("response")
    private CostingIteration response;

    public CostingIteration getResponse() {
        return this.response;
    }

    public CostingIteration setResponse(CostingIteration response) {
        return this.response = response;
    }

}
