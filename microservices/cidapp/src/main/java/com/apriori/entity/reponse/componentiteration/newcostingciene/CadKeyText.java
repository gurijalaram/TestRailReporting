package com.apriori.entity.reponse.componentiteration.newcostingciene;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CadKeyText {
    private String name;

    public String getName() {
        return name;
    }

    public CadKeyText setName(String name) {
        this.name = name;
        return this;
    }
}
