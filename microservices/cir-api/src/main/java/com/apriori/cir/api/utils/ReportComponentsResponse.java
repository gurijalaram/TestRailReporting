package com.apriori.cir.api.utils;

import com.apriori.shared.util.annotations.Schema;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@Schema(location = "ReportComponents.json")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReportComponentsResponse {
    @JsonProperty("reportConfig")
    private ReportConfigItem reportConfigItem;

    @JsonProperty("91544a69-eb37-47d2-8783-8f940ac904f0_40#1")
    private ChartInfoItem infoItemOne;

    @JsonProperty("91544a69-eb37-47d2-8783-8f940ac904f0_43#1")
    private ChartInfoItem infoItemTwo;

    @JsonProperty("91544a69-eb37-47d2-8783-8f940ac904f0_46#1")
    private ChartInfoItem infoItemThree;

    @JsonProperty("ffe91692-1124-45bf-89f7-c2120fa66886.0")
    private ChartInfoItemTypeTwo infoItemFour;

    @JsonProperty("435354b6-84b8-42be-b3ce-1d7acad695d1.1")
    private ChartInfoItem infoItemFive;
}
