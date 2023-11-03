package com.apriori.cid.api.models.request.operators;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class Query {

    private LogicalOperator filter;
    private Map<String, Object> parameters;


}
