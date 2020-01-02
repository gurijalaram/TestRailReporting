package com.apriori.apibase.http.builder.common.response.common;

import com.apriori.apibase.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

@Schema(location = "SelectionSchema.json")
public class SelectionEntity {

    @JsonProperty
    private String colour;

    @JsonProperty
    private boolean resourceCreated;

    public String getColour() {
        return colour;
    }

    public SelectionEntity setColour(String colour) {
        this.colour = colour;
        return this;
    }
}