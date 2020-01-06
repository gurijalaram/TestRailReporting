package com.apriori.apibase.http.builder.common.response.common;

import com.apriori.apibase.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

@Schema(location = "SelectionColourSchema.json")
public class SelectionColourEntity {

    @JsonProperty
    private String colour;

    @JsonProperty
    private boolean resourceCreated;

    public String getColour() {
        return colour;
    }

    public SelectionColourEntity setColour(String colour) {
        this.colour = colour;
        return this;
    }
}