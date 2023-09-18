package com.apriori.cir.utils;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;

@Data
public class DataItem {
    private ArrayList<ItemTypeOne> properties;
    @JsonProperty("xCategories")
    private ArrayList<String> xcategories;
    private ArrayList<ItemTypeTwo> series;
}
