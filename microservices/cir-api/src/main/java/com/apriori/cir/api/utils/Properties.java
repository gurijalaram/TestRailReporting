package com.apriori.cir.api.utils;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;

@Data
public class Properties {
    private ArrayList<Services> services;
    @JsonProperty("renderto")
    private String renderTo;
    private int width;
    private int height;
    private String prop;
    private String val;
}
