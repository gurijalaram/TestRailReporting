package com.apriori.cirapi.entity;

import com.apriori.cirapi.entity.response.ChartDataPoint;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jsoup.nodes.Document;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JasperReportSummary {
    private Document reportHtmlPart;
    private List<ChartDataPoint> chartDataPoints;

    public ChartDataPoint getChartDataPointByPartName(final String partName) {
        return chartDataPoints.stream().filter(dataPoint -> dataPoint.getPartName().equals(partName))
            .findFirst().orElse(null);
    }
}
