package com.apriori.entity.reponse.componentiteration;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class FaceIndex {
    private List<Integer> value = null;
    private Key key;

    public List<Integer> getValue() {
        return value;
    }

    public FaceIndex setValue(List<Integer> value) {
        this.value = value;
        return this;
    }

    public Key getKey() {
        return key;
    }

    public FaceIndex setKey(Key key) {
        this.key = key;
        return this;
    }
}
