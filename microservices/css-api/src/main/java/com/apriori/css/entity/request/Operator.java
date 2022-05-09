package com.apriori.css.entity.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public  class Operator {
    private Params equals;
    private Params starts;
    private Params in;
    private Params between;
    private Params isNull;
}
