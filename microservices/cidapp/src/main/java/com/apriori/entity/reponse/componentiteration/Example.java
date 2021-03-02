package com.apriori.entity.reponse.componentiteration;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Example {
    private ComponentIteration componentIteration;

    public ComponentIteration getComponentIteration() {
        return componentIteration;
    }

    public Example setComponentIteration(ComponentIteration componentIteration) {
        this.componentIteration = componentIteration;
        return this;
    }
}
