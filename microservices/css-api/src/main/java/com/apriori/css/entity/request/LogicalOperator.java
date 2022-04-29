package com.apriori.css.entity.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LogicalOperator {
    private List<Operator> or;
    private List<Operator> and;
    private List<Operator> wrongOperator;
    private Operator not;
}
