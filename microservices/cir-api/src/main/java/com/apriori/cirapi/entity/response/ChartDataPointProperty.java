package com.apriori.cirapi.entity.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ChartDataPointProperty {

    @JsonProperty("prop")
    private String property;

    @JsonProperty("val")
    private Object value;
}
