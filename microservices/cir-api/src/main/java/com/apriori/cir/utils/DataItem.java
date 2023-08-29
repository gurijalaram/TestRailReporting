package com.apriori.cir.utils;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DataItem {
    private ArrayList<Item> properties;
    @JsonProperty("xCategories")
    private ArrayList<String> xCategories;
    private ArrayList<Item2> series;
}
