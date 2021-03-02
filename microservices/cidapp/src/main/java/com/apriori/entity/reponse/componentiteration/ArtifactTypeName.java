package com.apriori.entity.reponse.componentiteration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArtifactTypeName {
    private String name;

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public ArtifactTypeName setName(String name) {
        this.name = name;
        return this;
    }
}
