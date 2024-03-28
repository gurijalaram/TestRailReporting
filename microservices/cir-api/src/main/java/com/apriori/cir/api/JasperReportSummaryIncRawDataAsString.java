package com.apriori.cir.api;

import com.apriori.cir.api.models.response.ChartData;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class JasperReportSummaryIncRawDataAsString {
    private String chartDataRawAsString;
    private List<ChartData> chartData;
    private Document reportHtmlPart;
}
