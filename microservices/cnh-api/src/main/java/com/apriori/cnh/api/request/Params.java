package com.apriori.cnh.api.request;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Params {
    private String name;
    private String value;
    private String type;
}
