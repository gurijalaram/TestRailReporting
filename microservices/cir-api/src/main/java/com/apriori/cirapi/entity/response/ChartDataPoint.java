package com.apriori.cirapi.entity.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChartDataPoint {
    @JsonProperty("name")
    private String partName;

    private List<ChartDataPointProperty> properties;

    public ChartDataPointProperty getPropertyByName(final String name) {
        return properties.stream().filter(property -> name.equals(property.getProperty()))
            .findFirst().orElse(null);
    }
}
