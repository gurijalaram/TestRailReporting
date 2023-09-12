package com.apriori.cir.utils;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Services {
    private String service;
    @JsonProperty("data")
    private Object dataItem;
}
