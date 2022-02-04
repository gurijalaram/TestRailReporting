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

    @JsonProperty("x")
    private String finishMass;

    @JsonProperty("y")
    private String fullyBurdenedCost;

    private List<ChartDataPointProperty> properties;

    public String getMassMetric() {
        return (String) getPropertyValueIfExist("massMetric");
    }

    public String getCostMetric() {
        return (String) getPropertyValueIfExist("costMetric");
    }

    public String getDTCScore() {
        return (String) getPropertyValueIfExist("dtcScore");
    }

    public Double getAnnualSpend() {
        return (Double) getPropertyValueIfExist("annualSpend");
    }

    private Object getPropertyValueIfExist(final String propertyName) {
        ChartDataPointProperty chartDataPointProperty = getPropertyByName(propertyName);

        return chartDataPointProperty != null ? chartDataPointProperty.getValue() : null;
    }

    public ChartDataPointProperty getPropertyByName(final String name) {
        return properties.stream().filter(property -> name.equals(property.getProperty()))
            .findFirst().orElse(null);
    }
}
