package com.apriori.cir.utils;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;

@Data
public class Params {
    @JsonProperty("_report")
    private String report;
    private ArrayList<String> exportSetName;
    private String startDate;
    private String endDate;
    private ArrayList<String> department;
    private ArrayList<String> location;
    private String trendingPeriod;
    private ArrayList<String> costSource;
}
