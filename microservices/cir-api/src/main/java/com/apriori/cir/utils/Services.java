package com.apriori.cir.utils;

import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
public class Services {
    private String service;
    @JsonProperty("data")
    private DataItem dataItem;
}
