package com.apriori.entity.reponse.componentiteration.newcostingciene;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Width {
    private String name;
    private String unitTypeName;

    public String getName() {
        return name;
    }

    public Width setName(String name) {
        this.name = name;
        return this;
    }

    public String getUnitTypeName() {
        return unitTypeName;
    }

    public Width setUnitTypeName(String unitTypeName) {
        this.unitTypeName = unitTypeName;
        return this;
    }
}
