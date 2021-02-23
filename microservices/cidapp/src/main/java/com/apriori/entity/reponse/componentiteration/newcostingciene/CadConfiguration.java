package com.apriori.entity.reponse.componentiteration.newcostingciene;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CadConfiguration {
    private String name;

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public CadConfiguration setName(String name) {
        this.name = name;
        return this;
    }
}
