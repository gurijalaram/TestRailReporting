package com.apriori.apibase.services.response.objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CustomizeeKeyEntity {
    @JsonProperty
    private String first = "TableModelInfo";

    @JsonProperty
    private String second = "workspaceTable";

    @JsonProperty
    private String typeName = "TableModelInfo";

    @JsonProperty
    private String name = "workspaceTable";
} 
