package com.apriori.cirapi.entity.response;

import com.apriori.cirapi.entity.enums.ReportChartType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChartData {
    private ReportChartType chartType;
    private List<ChartDataPoint> chartDataPoints;

    public ChartDataPoint getChartDataPointByPartName(final String partName) {
        return chartDataPoints.stream().filter(dataPoint -> dataPoint.getPartName().equals(partName))
            .findFirst().orElse(null);
    }
}
