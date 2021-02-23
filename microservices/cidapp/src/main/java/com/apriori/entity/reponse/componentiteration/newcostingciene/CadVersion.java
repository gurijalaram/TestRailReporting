package com.apriori.entity.reponse.componentiteration.newcostingciene;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CadVersion {
    private String name;

    public String getName() {
        return name;
    }

    public CadVersion setName(String name) {
        this.name = name;
        return this;
    }
}
