package com.apriori.cidapp.entity.response.componentiteration;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Value {
    private List<Double> origin = null;
    private List<Double> direction = null;
    private Double length;

    public List<Double> getOrigin() {
        return origin;
    }

    public Value setOrigin(List<Double> origin) {
        this.origin = origin;
        return this;
    }

    public List<Double> getDirection() {
        return direction;
    }

    public Value setDirection(List<Double> direction) {
        this.direction = direction;
        return this;
    }

    public Double getLength() {
        return length;
    }

    public Value setLength(Double length) {
        this.length = length;
        return this;
    }
}
