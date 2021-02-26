package com.apriori.entity.reponse.componentiteration;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class NumSideCores {
    private String name;

    public String getName() {
        return name;
    }

    public NumSideCores setName(String name) {
        this.name = name;
        return this;
    }
}
