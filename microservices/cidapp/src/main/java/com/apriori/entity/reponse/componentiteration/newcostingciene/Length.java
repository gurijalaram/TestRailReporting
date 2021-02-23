package com.apriori.entity.reponse.componentiteration.newcostingciene;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Length {
    private String name;
    private String unitTypeName;

    public String getName() {
        return name;
    }

    public Length setName(String name) {
        this.name = name;
        return this;
    }

    public String getUnitTypeName() {
        return unitTypeName;
    }

    public Length setUnitTypeName(String unitTypeName) {
        this.unitTypeName = unitTypeName;
        return this;
    }
}
