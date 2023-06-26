package com.apriori.cmp.entity.builder;


import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ComparisonObjectBuilder {
    private String externalIdentity;
    private Integer position;
    private Boolean basis;
}
