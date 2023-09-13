package com.apriori.cir;

import com.apriori.cir.models.response.ChartData;
import com.apriori.cir.utils.ReportComponentsResponse;

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
public class JasperReportSummaryIncRawData {
    private ReportComponentsResponse chartDataRaw;
    private List<ChartData> chartData;
    private Document reportHtmlPart;
}
