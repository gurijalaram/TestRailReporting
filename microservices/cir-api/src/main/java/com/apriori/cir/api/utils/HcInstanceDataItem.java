package com.apriori.cir.api.utils;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;

@Data
public class HcInstanceDataItem {
    private ArrayList<Properties> properties;
    private ArrayList<Services> services;
    @JsonProperty("renderto")
    private String renderTo;
    private int width;
    private int height;
}
