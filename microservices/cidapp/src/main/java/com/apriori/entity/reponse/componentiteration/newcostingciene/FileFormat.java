package com.apriori.entity.reponse.componentiteration.newcostingciene;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class FileFormat {
    private String name;

    public String getName() {
        return name;
    }

    public FileFormat setName(String name) {
        this.name = name;
        return this;
    }
}
