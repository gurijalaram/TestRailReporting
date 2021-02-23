
package com.apriori.entity.reponse.componentiteration.newcostingciene;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class EngineType {
    private String name;

    public String getName() {
        return name;
    }

    public EngineType setName(String name) {
        this.name = name;
        return this;
    }
}
