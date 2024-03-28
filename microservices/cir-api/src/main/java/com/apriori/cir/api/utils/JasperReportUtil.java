package com.apriori.cir.api.utils;

import com.apriori.cir.api.JasperReportSummary;
import com.apriori.cir.api.JasperReportSummaryIncRawData;
import com.apriori.cir.api.JasperReportSummaryIncRawDataAsString;
import com.apriori.cir.api.enums.JasperApiInputControlsPathEnum;
import com.apriori.cir.api.enums.ReportChartType;
import com.apriori.cir.api.models.request.ReportExportRequest;
import com.apriori.cir.api.models.request.ReportRequest;
import com.apriori.cir.api.models.response.ChartData;
import com.apriori.cir.api.models.response.ChartDataPoint;
import com.apriori.cir.api.models.response.ChartDataPointProperty;
import com.apriori.cir.api.models.response.InputControl;
import com.apriori.cir.api.models.response.ReportStatusResponse;
import com.apriori.shared.util.http.models.entity.RequestEntity;
import com.apriori.shared.util.http.models.request.HTTPRequest;
import com.apriori.shared.util.http.utils.ResponseWrapper;

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

@Slf4j
public class JasperReportUtil {

    private ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private long WAIT_TIME = 30;

    private String jasperSessionValue = "JSESSIONID=%s";


    public static JasperReportUtil init(final String jasperSessionId) {
        return new JasperReportUtil(jasperSessionId);
    }

    public JasperReportUtil(final String jasperSessionId) {
        this.jasperSessionValue = String.format(jasperSessionValue, jasperSessionId);
    }

    // TODO z: fix it threads
    public InputControl getInputControls(JasperApiInputControlsPathEnum value) {
        RequestEntity requestEntity = new RequestEntity()
            .endpoint(value)
            .returnType(InputControl.class)
            .headers(initHeadersWithJSession())
            .inlineVariables("%20")
            .expectedResponseCode(HttpStatus.SC_OK)
            .urlEncodingEnabled(false);

        if (value.toString().startsWith("SHEET")) {
            requestEntity.inlineVariables("%20", "%20");
        }

        ResponseWrapper<InputControl> responseResponseWrapper = HTTPRequest.build(requestEntity).post();

        return responseResponseWrapper.getResponseEntity();
    }

    public JasperReportSummary generateJasperReportSummary(ReportRequest reportRequest) {
        ReportStatusResponse response = this.generateReport(reportRequest);
        ReportStatusResponse exportedReport = this.doReportExport(response);

        this.waitUntilReportReady(response.getRequestId(),
            exportedReport.getId());

        String requestId = response.getRequestId();
        String reportId = exportedReport.getId();

        return JasperReportSummary.builder()
            .reportHtmlPart(this.getReportHtmlData(requestId,
                reportId)
            )
            .chartData(this.getReportChartData(requestId,
                reportId)
            )
            .build();
    }

    public JasperReportSummaryIncRawData generateJasperReportSummaryIncRawData(ReportRequest reportRequest) {
        ReportStatusResponse response = this.generateReport(reportRequest);
        ReportStatusResponse exportedReport = this.doReportExport(response);

        this.waitUntilReportReady(response.getRequestId(),
            exportedReport.getId());

        String requestId = response.getRequestId();
        String reportId = exportedReport.getId();

        return JasperReportSummaryIncRawData.builder()
            .reportHtmlPart(this.getReportHtmlData(requestId,
                reportId)
            )
            .chartData(this.getReportChartData(requestId,
                reportId)
            )
            .chartDataRaw(this.getReportChartDataRaw(requestId,
                reportId))
            .build();
    }

    public JasperReportSummaryIncRawDataAsString generateJasperReportSummaryIncRawDataAsString(ReportRequest reportRequest) {
        ReportStatusResponse response = this.generateReport(reportRequest);
        ReportStatusResponse exportedReport = this.doReportExport(response);

        this.waitUntilReportReady(response.getRequestId(),
            exportedReport.getId());

        String requestId = response.getRequestId();
        String reportId = exportedReport.getId();

        return JasperReportSummaryIncRawDataAsString.builder()
            .reportHtmlPart(this.getReportHtmlData(requestId,
                reportId)
            )
            .chartData(this.getReportChartData(requestId,
                reportId)
            )
            .chartDataRawAsString(this.getReportChartDataRawAsString(requestId,
                reportId))
            .build();
    }

    @SneakyThrows
    private void waitUntilReportReady(String requestId, String exportId) {
        RequestEntity requestEntity = new RequestEntity()
            .endpoint(JasperApiInputControlsPathEnum.REPORT_OUTPUT_STATUS_BY_REQUEST_EXPORT_IDs)
            .returnType(null)
            .inlineVariables(requestId, exportId)
            .headers(initHeadersWithJSession())
            .expectedResponseCode(HttpStatus.SC_OK);

        long initialTime = System.currentTimeMillis() / 1000;

        String responseBodyText;
        do {
            responseBodyText = HTTPRequest.build(requestEntity).get().getBody();
        } while (!responseBodyText.contains("ready")
            && !responseBodyText.contains("failed")
            && ((System.currentTimeMillis() / 1000) - initialTime) < WAIT_TIME);
    }

    private ReportStatusResponse generateReport(ReportRequest reportRequest) {
        RequestEntity requestEntity = new RequestEntity()
            .endpoint(JasperApiInputControlsPathEnum.REPORT_EXECUTIONS)
            .returnType(ReportStatusResponse.class)
            .headers(initHeadersWithJSession())
            .body(reportRequest)
            .expectedResponseCode(HttpStatus.SC_OK);

        ResponseWrapper<ReportStatusResponse> responseResponseWrapper = HTTPRequest.build(requestEntity).post();

        return responseResponseWrapper.getResponseEntity();
    }

    private ReportStatusResponse doReportExport(ReportStatusResponse response) {
        RequestEntity doExportRequest = new RequestEntity()
            .endpoint(JasperApiInputControlsPathEnum.REPORT_EXPORT_BY_REQUEST_ID)
            .returnType(ReportStatusResponse.class)
            .inlineVariables(response.getRequestId())
            .headers(initHeadersWithJSession())
            .body(ReportExportRequest.initFromJsonFile())
            .expectedResponseCode(HttpStatus.SC_OK);

        ResponseWrapper<ReportStatusResponse> exportDataResponseWrapper = HTTPRequest.build(doExportRequest).post();

        return exportDataResponseWrapper.getResponseEntity();
    }

    @SneakyThrows
    private Document getReportHtmlData(final String requestId, final String exportId) {
        RequestEntity requestEntity = new RequestEntity()
            .endpoint(JasperApiInputControlsPathEnum.REPORT_OUTPUT_RESOURCE_BY_REQUEST_EXPORT_IDs)
            .returnType(InputStream.class)
            .inlineVariables(requestId, exportId)
            .headers(initHeadersWithJSession())
            .expectedResponseCode(HttpStatus.SC_OK);

        InputStream htmlData = (InputStream) HTTPRequest.build(requestEntity).get().getResponseEntity();

        return Jsoup.parse(htmlData, "UTF-8", "/aPriori/reports/");
    }

    @SneakyThrows
    private List<ChartData> getReportChartData(final String requestId, final String exportId) {
        RequestEntity requestEntity = new RequestEntity()
            .endpoint(JasperApiInputControlsPathEnum.REPORT_OUTPUT_COMPONENT_JSON_BY_REQUEST_EXPORT_IDs)
            .inlineVariables(requestId, exportId)
            .headers(initHeadersWithJSession())
            .expectedResponseCode(HttpStatus.SC_OK);

        return parseJsonResponse(HTTPRequest.build(requestEntity).get()
            .getBody()
        );
    }

    @SneakyThrows
    private ReportComponentsResponse getReportChartDataRaw(final String requestId, final String exportId) {
        RequestEntity requestEntity = new RequestEntity()
            .endpoint(JasperApiInputControlsPathEnum.REPORT_OUTPUT_COMPONENT_JSON_BY_REQUEST_EXPORT_IDs)
            .returnType(ReportComponentsResponse.class)
            .inlineVariables(requestId, exportId)
            .headers(initHeadersWithJSession())
            .expectedResponseCode(HttpStatus.SC_OK);

        return (ReportComponentsResponse) HTTPRequest.build(requestEntity).get().getResponseEntity();
    }

    @SneakyThrows
    private String getReportChartDataRawAsString(final String requestId, final String exportId) {
        RequestEntity requestEntity = new RequestEntity()
            .endpoint(JasperApiInputControlsPathEnum.REPORT_OUTPUT_COMPONENT_JSON_BY_REQUEST_EXPORT_IDs)
            .returnType(ReportComponentsResponse.class)
            .inlineVariables(requestId, exportId)
            .headers(initHeadersWithJSession())
            .expectedResponseCode(HttpStatus.SC_OK);

        return HTTPRequest.build(requestEntity).get().getBody();
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
