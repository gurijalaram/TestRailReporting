package com.apriori.entity.reponse.componentiteration;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DrawableNode {
    private Integer value;
    private Key key;

    public Integer getValue() {
        return value;
    }

    public DrawableNode setValue(Integer value) {
        this.value = value;
        return this;
    }

    public Key getKey() {
        return key;
    }

    public DrawableNode setKey(Key key) {
        this.key = key;
        return this;
    }
}
