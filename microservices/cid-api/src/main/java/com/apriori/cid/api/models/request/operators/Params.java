package com.apriori.cid.api.models.request.operators;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public  class Params {
    private String property;
    private Object value;
    private List<Object> values;
    private Object min;
    private Object max;
}
