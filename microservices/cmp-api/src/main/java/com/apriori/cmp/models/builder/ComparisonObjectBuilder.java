package com.apriori.cmp.models.builder;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ComparisonObjectBuilder {
    private String externalIdentity;
    private Integer position;
    private Boolean basis;
}
