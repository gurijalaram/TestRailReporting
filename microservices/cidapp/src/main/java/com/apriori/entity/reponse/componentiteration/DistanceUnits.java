
package com.apriori.entity.reponse.componentiteration;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DistanceUnits {
    private String name;

    public String getName() {
        return name;
    }

    public DistanceUnits setName(String name) {
        this.name = name;
        return this;
    }

}