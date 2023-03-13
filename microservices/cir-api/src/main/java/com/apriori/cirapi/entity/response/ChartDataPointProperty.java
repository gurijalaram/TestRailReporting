package com.apriori.cirapi.entity.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ChartDataPointProperty {

    public ChartDataPointProperty() {
        // Default constructor
    }

    public ChartDataPointProperty(String property, Object value) {
        this.property = property;
        this.value = value;
    }

    @JsonProperty("prop")
    private String property;

    @JsonProperty("val")
    private Object value;
}
