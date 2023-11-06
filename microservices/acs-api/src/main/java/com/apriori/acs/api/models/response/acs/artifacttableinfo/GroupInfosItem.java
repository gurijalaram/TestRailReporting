package com.apriori.acs.api.models.response.acs.artifacttableinfo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GroupInfosItem {
    @JsonProperty("@type")
    private String type;
    private String name;
    private String cachedDisplayName;
    private String columnNames;
    private Boolean isNullGroup;
    private ColumnNameIndexItem columnNameIndex;
    private String expandFromColumn;
    private Boolean nullGroup;
    private Boolean expandLeft;
}
