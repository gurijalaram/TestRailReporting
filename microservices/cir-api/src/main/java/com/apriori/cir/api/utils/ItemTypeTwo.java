package com.apriori.cir.api.utils;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;

@Data
public class ItemTypeTwo {
    private String name;
    @SuppressWarnings("checkstyle:MemberName")
    @JsonProperty("_jrid")
    private String jrID;
    private ArrayList<ItemTypeOne> properties;
    private ArrayList<ItemTypeThree> data;
}
