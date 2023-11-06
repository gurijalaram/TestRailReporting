package com.apriori.cmp.api.models.request;


import com.apriori.cmp.api.models.builder.ComparisonObjectBuilder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateComparison {
    private String comparisonName;
    private String comparisonType;
    @JsonProperty("objectType")
    private String comparisonObjectType;
    @JsonProperty("comparisonObjects")
    private List<ComparisonObjectBuilder> objectsToCompare;
}
