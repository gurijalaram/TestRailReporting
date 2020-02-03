package com.apriori.apibase.http.builder.common.response.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.util.List;


@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
@JsonTypeName("tableViewColumnGroupInfo")
@JsonPropertyOrder({"children", "displayName"})
public class ColumnCollectionEntity {
    @JsonProperty("children")
    private List<ColumnEntity> children;

    @JsonProperty
    final String displayName = "TableView.rootDisplayName";

    public ColumnCollectionEntity(List<ColumnEntity> columns) {
        this.children = columns;
    }
}