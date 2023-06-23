package com.apriori.cmp.entity.request;


import com.apriori.cmp.entity.builder.ComparisonObjectBuilder;

import com.fasterxml.jackson.annotation.JsonInclude;
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
    private String objectType;
    private List<ComparisonObjectBuilder> objects;
}
