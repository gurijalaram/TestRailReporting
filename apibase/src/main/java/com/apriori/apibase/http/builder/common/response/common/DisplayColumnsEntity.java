package com.apriori.apibase.http.builder.common.response.common;

import com.apriori.apibase.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@Schema(location = "DisplayColumnsSchema.json")
public class DisplayColumnsEntity {

    @JsonProperty
    private CustomizeeKeyEntity customizeeKey = new CustomizeeKeyEntity();

    @JsonProperty
    private String name;

    @JsonProperty
    private ColumnCollectionEntity root;

    public DisplayColumnsEntity setChildren(List<ColumnEntity> columns) {
        this.root = new ColumnCollectionEntity(columns);
        return this;
    }

    public DisplayColumnsEntity setName(String name) {
        this.name = name;
        return this;
    }
}