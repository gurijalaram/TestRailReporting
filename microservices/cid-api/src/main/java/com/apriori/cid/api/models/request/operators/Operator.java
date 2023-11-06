package com.apriori.cid.api.models.request.operators;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public  class Operator {
    private Params equals;
    private Params greater;
    private Params ends;
    private Params contains;
    private Params less;
    private Params greaterEquals;
    private Params starts;
    private Params in;
    private Params between;
    private Params isNull;
    private List<Operator> or;
    private List<Operator> and;
}
