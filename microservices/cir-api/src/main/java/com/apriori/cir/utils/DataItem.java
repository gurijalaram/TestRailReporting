package com.apriori.cir.utils;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;

@Data
public class DataItem {
    private ArrayList<Item> properties;
    @JsonProperty("xCategories")
    private ArrayList<String> xcategories;
    private ArrayList<Item2> series;
}
