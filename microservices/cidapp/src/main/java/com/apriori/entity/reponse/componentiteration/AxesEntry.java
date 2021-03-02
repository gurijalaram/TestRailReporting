package com.apriori.entity.reponse.componentiteration;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AxesEntry {
    private List<Value> value = null;
    private Key key;

    public List<Value> getValue() {
        return value;
    }

    public AxesEntry setValue(List<Value> value) {
        this.value = value;
        return this;
    }

    public Key getKey() {
        return key;
    }

    public AxesEntry setKey(Key key) {
        this.key = key;
        return this;
    }
}
