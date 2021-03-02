package com.apriori.entity.reponse.componentiteration;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Volume {
    private String name;
    private String unitTypeName;

    public String getName() {
        return name;
    }

    public Volume setName(String name) {
        this.name = name;
        return this;
    }

    public String getUnitTypeName() {
        return unitTypeName;
    }

    public Volume setUnitTypeName(String unitTypeName) {
        this.unitTypeName = unitTypeName;
        return this;
    }
}