package com.apriori.apibase.http.builder.common.response.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;


@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
@JsonTypeName("column")
public class ColumnEntity {
    @JsonProperty
    private Boolean editable;

    @JsonProperty
    private String name;

    @JsonProperty
    private String displayName;

    public ColumnEntity setEditable(Boolean editable) {
        this.editable = editable;
        return this;
    }

    public ColumnEntity setName(String name) {
        this.name = name;
        return this;
    }

    public ColumnEntity setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }
}
