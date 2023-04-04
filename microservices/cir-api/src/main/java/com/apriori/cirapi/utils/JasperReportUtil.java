package com.apriori.cirapi.utils;

import com.apriori.cirapi.entity.JasperReportSummary;
import com.apriori.cirapi.entity.enums.CirApiEnum;
import com.apriori.cirapi.entity.enums.ReportChartType;
import com.apriori.cirapi.entity.request.ReportExportRequest;
import com.apriori.cirapi.entity.request.ReportRequest;
import com.apriori.cirapi.entity.response.ChartData;
import com.apriori.cirapi.entity.response.ChartDataPoint;
import com.apriori.cirapi.entity.response.ChartDataPointProperty;
import com.apriori.cirapi.entity.response.InputControl;
import com.apriori.cirapi.entity.response.ReportStatusResponse;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
public class JasperReportUtil {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final long WAIT_TIME = 30;

    private String jasperSessionValue = "JSESSIONID=%s";


    public static JasperReportUtil init(final String jasperSessionId) {
        return new JasperReportUtil(jasperSessionId);
    }

    public JasperReportUtil(final String jasperSessionId) {
        this.jasperSessionValue = String.format(jasperSessionValue, jasperSessionId);
    }

    public InputControl getInputControls() {
        RequestEntity requestEntity = RequestEntityUtil.init(CirApiEnum.DTC_METRICS, InputControl.class)
            .headers(initHeadersWithJSession())
            .inlineVariables("%20")
            .expectedResponseCode(HttpStatus.SC_OK)
            .urlEncodingEnabled(false);

        ResponseWrapper<InputControl> responseResponseWrapper = HTTPRequest.build(requestEntity).post();

        return responseResponseWrapper.getResponseEntity();
    }

    public JasperReportSummary generateJasperReportSummary(ReportRequest reportRequest) {
        ReportStatusResponse response = this.generateReport(reportRequest);
        ReportStatusResponse exportedReport = this.doReportExport(response);

        this.waitUntilReportReady(response.getRequestId(),
            exportedReport.getId());

        return JasperReportSummary.builder()
            .reportHtmlPart(this.getReportHtmlData(response.getRequestId(),
                exportedReport.getId())
            )
            .chartData(this.getReportChartData(response.getRequestId(),
                exportedReport.getId())
            )
            .build();
    }

    @SneakyThrows
    private void waitUntilReportReady(String requestId, String exportId) {
        RequestEntity requestEntity = RequestEntityUtil.init(CirApiEnum.REPORT_OUTPUT_STATUS_BY_REQUEST_EXPORT_IDs, null)
            .inlineVariables(requestId, exportId)
            .headers(initHeadersWithJSession())
            .expectedResponseCode(HttpStatus.SC_OK);

        long initialTime = System.currentTimeMillis() / 1000;

        do {
            TimeUnit.SECONDS.sleep(5);

        } while (!HTTPRequest.build(requestEntity).get().getBody().contains("ready")
            || ((System.currentTimeMillis() / 1000) - initialTime) < WAIT_TIME);
    }

    private ReportStatusResponse generateReport(ReportRequest reportRequest) {
        RequestEntity requestEntity = RequestEntityUtil.init(CirApiEnum.REPORT_EXECUTIONS, ReportStatusResponse.class)
            .headers(initHeadersWithJSession())
            .body(reportRequest)
            .expectedResponseCode(HttpStatus.SC_OK);

        ResponseWrapper<ReportStatusResponse> responseResponseWrapper = HTTPRequest.build(requestEntity).post();

        return responseResponseWrapper.getResponseEntity();
    }

    private ReportStatusResponse doReportExport(ReportStatusResponse response) {
        RequestEntity doExportRequest = RequestEntityUtil.init(CirApiEnum.REPORT_EXPORT_BY_REQUEST_ID, ReportStatusResponse.class)
            .inlineVariables(response.getRequestId())
            .headers(initHeadersWithJSession())
            .body(ReportExportRequest.initFromJsonFile())
            .expectedResponseCode(HttpStatus.SC_OK);

        ResponseWrapper<ReportStatusResponse> exportDataResponseWrapper = HTTPRequest.build(doExportRequest).post();

        return exportDataResponseWrapper.getResponseEntity();
    }

    @SneakyThrows
    private Document getReportHtmlData(final String requestId, final String exportId) {
        RequestEntity requestEntity = RequestEntityUtil.init(CirApiEnum.REPORT_OUTPUT_RESOURCE_BY_REQUEST_EXPORT_IDs, InputStream.class)
            .inlineVariables(requestId, exportId)
            .headers(initHeadersWithJSession())
            .expectedResponseCode(HttpStatus.SC_OK);

        InputStream htmlData = (InputStream) HTTPRequest.build(requestEntity).get().getResponseEntity();

        return Jsoup.parse(htmlData, "UTF-8", "/aPriori/reports/");
    }

    private List<ChartData> getReportChartData(final String requestId, final String exportId) {
        RequestEntity requestEntity = RequestEntityUtil.init(CirApiEnum.REPORT_OUTPUT_COMPONENT_JSON_BY_REQUEST_EXPORT_IDs, null)
            .inlineVariables(requestId, exportId)
            .headers(initHeadersWithJSession())
            .expectedResponseCode(HttpStatus.SC_OK);

        return parseJsonResponse(HTTPRequest.build(requestEntity).get()
            .getBody()
        );
    }

    @SneakyThrows
    private List<ChartData> parseJsonResponse(final String jsonResponse) {
        final JsonNode dataNode = OBJECT_MAPPER.readTree(jsonResponse);
        List<ChartData> parsedChartData = new ArrayList<>();

        for (JsonNode node : dataNode) {
            ChartData chartData = new ChartData();
            JsonNode chartTypeNode = node.findValue("charttype");

            if (chartTypeNode == null) {
                log.warn("Chart type is not present in JSON response.");
                continue;
            }

            ReportChartType reportChartType = ReportChartType.get(chartTypeNode.asText());

            if (reportChartType == null) {
                log.warn("Failed to parse chart response. Chart type {} ", chartTypeNode.asText());
                continue;
            }

            chartData.setChartType(reportChartType);
            chartData.setChartDataPoints(
                this.parseChart(reportChartType, node)
            );

            parsedChartData.add(chartData);
        }

        return parsedChartData;
    }

    private List<ChartDataPoint> parseChart(final ReportChartType chartType, final JsonNode dataNode) {
        switch (chartType) {
            case BUBBLE_SCATTER:
                return parseBubbleChart(dataNode);
            case BAR:
            case STACKED_BAR:
                return parseStackedBar(dataNode);
            default:
                throw new IllegalArgumentException("Failed to parse chart: " + chartType);
        }
    }

    @SneakyThrows
    private List<ChartDataPoint> parseBubbleChart(JsonNode dataNode) {

        dataNode = dataNode.findValue("series");

        if (dataNode != null) {
            dataNode = dataNode.findValue("data");
        }

        return dataNode != null ? OBJECT_MAPPER.readerFor(new TypeReference<List<ChartDataPoint>>() {
        }).readValue(dataNode) : null;
    }

    @SneakyThrows
    private List<ChartDataPoint> parseStackedBar(JsonNode dataNode) {

        List<ChartDataPoint> mappedChartDataPoints = new ArrayList<>();

        JsonNode partNames = dataNode.findValue("xCategories");
        JsonNode chartValues = dataNode.findValue("series");

        for (int i = 0; i < partNames.size(); i++) {
            ChartDataPoint chartDataPoint = new ChartDataPoint();
            List<ChartDataPointProperty> chartDataPointProperties = new ArrayList<>();

            for (JsonNode chart : chartValues) {
                JsonNode property = chart.findValue("name");
                JsonNode value = chart.findValue("data").get(i).findValue("y");

                if (property != null && value != null) {
                    chartDataPointProperties.add(new ChartDataPointProperty(property.asText(), value.asText()));
                }
            }

            chartDataPoint.setPartName(partNames.get(i).asText());
            chartDataPoint.setProperties(chartDataPointProperties);
            mappedChartDataPoints.add(chartDataPoint);
        }

        return mappedChartDataPoints;
    }

    private HashMap<String, String> initHeadersWithJSession() {
        return new HashMap<String, String>() {
            {
                put("Cookie", "userLocale=en_US; userTimezone=America/New_York; ".concat(jasperSessionValue));
            }
        };
    }
}
