package com.apriori.cir.api.models.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Objects;

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

    public Integer getHoleIssues() {
        return this.getNotNullIntPropertyValue("Hole Issues");
    }

    public Integer getMaterialIssues() {
        return this.getNotNullIntPropertyValue("Material Issues");
    }

    public Integer getRadiusIssues() {
        return this.getNotNullIntPropertyValue("Radius Issues");
    }

    public Integer getDraftIssues() {
        return this.getNotNullIntPropertyValue("Draft Issues");
    }

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

    private Integer getNotNullIntPropertyValue(String propertyName) {
        return Integer.valueOf((String) Objects.requireNonNull(getPropertyValueIfExist(propertyName)));
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
