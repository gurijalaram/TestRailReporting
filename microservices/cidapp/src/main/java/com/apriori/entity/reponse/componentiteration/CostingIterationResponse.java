package com.apriori.entity.reponse.componentiteration;

import com.apriori.utils.http.enums.Schema;

@Schema(location = "newcid/ComponentIterationsResponse.json")
public class CostingIterationResponse {
    private CostingIteration response;

    public CostingIteration getResponse() {
        return this.response;
    }
}
