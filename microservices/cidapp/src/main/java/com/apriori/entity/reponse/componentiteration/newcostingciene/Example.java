package com.apriori.entity.reponse.componentiteration.newcostingciene;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Example {
    private CostingIteration costingIteration;

    public CostingIteration getCostingIteration() {
        return costingIteration;
    }

    public Example setCostingIteration(CostingIteration costingIteration) {
        this.costingIteration = costingIteration;
        return this;
    }
}
