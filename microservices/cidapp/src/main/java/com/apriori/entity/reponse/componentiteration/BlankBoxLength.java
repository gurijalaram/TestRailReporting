package com.apriori.entity.reponse.componentiteration;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BlankBoxLength {
    private String name;
    private String unitTypeName;

    public String getName() {
        return name;
    }

    public BlankBoxLength setName(String name) {
        this.name = name;
        return this;
    }

    public String getUnitTypeName() {
        return unitTypeName;
    }

    public BlankBoxLength setUnitTypeName(String unitTypeName) {
        this.unitTypeName = unitTypeName;
        return this;
    }
}
